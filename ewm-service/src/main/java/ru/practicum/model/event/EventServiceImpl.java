package ru.practicum.model.event;


import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.HitClient;
import ru.practicum.HitDto;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.CategoryRepository;
import ru.practicum.model.event.eventDto.EventFullDto;
import ru.practicum.model.event.eventDto.NewEventDto;
import ru.practicum.model.event.eventDto.UpdateEventAdminRequest;
import ru.practicum.model.event.eventDto.UpdateEventUserRequest;
import ru.practicum.model.exeption.AlreadyExistException;
import ru.practicum.model.exeption.NoDataException;
import ru.practicum.model.exeption.ValidationException;
import ru.practicum.model.location.Location;
import ru.practicum.model.location.LocationMapper;
import ru.practicum.model.location.LocationRepository;
import ru.practicum.model.user.User;
import ru.practicum.model.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final HitClient hitClient;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Transactional
    @Override
    public EventFullDto addEvent(NewEventDto newEventDto, Long userId, String path) {
        if (newEventDto.getParticipantLimit() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неправильно задан параметр: лимит участников не может быть отрицательным");
        }
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> new NoDataException("Категории не существует"));
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NoDataException("Пользователя не существует"));

        int views = 0;
        Location location = locationRepository.save(LocationMapper.mapToLocation(newEventDto.getLocation()));
        return EventMapper.mapToEventFullDto(eventRepository.save(EventMapper
                .mapToEvent(newEventDto, category, initiator, location)), views);
    }

    @Transactional
    @Override
    public EventFullDto updateEventByAdmin(UpdateEventAdminRequest adminEvent, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoDataException("События не существует"));

        if (adminEvent.getAnnotation() != null) {
            event.setAnnotation(adminEvent.getAnnotation());
        }
        if (adminEvent.getDescription() != null) {
            event.setDescription(adminEvent.getDescription());
        }
        if (adminEvent.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(adminEvent.getEventDate(), formatter);
            if (eventDate.isBefore(LocalDateTime.now())) {
                throw new ValidationException("Дата не может быть в прошлом");
            }
            event.setEventDate(eventDate);
        }
        if (adminEvent.getLocation() != null) {
            Location location = locationRepository.save(LocationMapper.mapToLocation(adminEvent.getLocation()));
            event.setLocation(location);
        }
        if (adminEvent.getPaid() != null) {
            event.setPaid(adminEvent.getPaid());
        }
        if (adminEvent.getParticipantLimit() != 0) {
            event.setParticipantLimit(adminEvent.getParticipantLimit());
        }
        if (adminEvent.getRequestModeration() != null) {
            event.setRequestModeration(adminEvent.getRequestModeration());
        }
        if (adminEvent.getStateAction() != null) {
            if (event.getState().equals(State.PUBLISHED) || event.getState().equals(State.CANCELED)) {
                throw new AlreadyExistException("Событие уже опубликовано");
            }
            event.setState(State.PUBLISHED);
            if (adminEvent.getStateAction().equals(State.REJECT_EVENT)) {
                event.setState(State.CANCELED);
            }
        }
        if (adminEvent.getTitle() != null) {
            event.setTitle(adminEvent.getTitle());
        }
        return EventMapper.mapToEventFullDto(eventRepository.save(event), 5);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsByParam(List<Long> initiatorIds,
                                               List<String> states,
                                               List<Long> categoriesIds,
                                               String rangeStart,
                                               String rangeEnd,
                                               Integer from,
                                               Integer size) {

        PageRequest page = PageRequest.of(from / size, size);
        Iterable<Event> foundEvents = null;
        List<State> listOfStates = null;
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (states != null) {
            listOfStates = states.stream()
                    .map(s -> State.from(s).orElseThrow(() -> new ValidationException("Unknown state: " + s)))
                    .collect(Collectors.toList());
        }
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, formatter);
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, formatter);
            if (end.isBefore(LocalDateTime.now())) {
                throw new ValidationException("rangeEnd не может быть в прошлом");
            }
        }
        if (initiatorIds != null
                && states != null
                && categoriesIds != null
                && rangeStart != null
                && rangeEnd != null
        ) {
            BooleanExpression byUserId = QEvent.event.initiator.id.in(initiatorIds);
            BooleanExpression byStates = QEvent.event.state.in(listOfStates);
            BooleanExpression byCategoryId = QEvent.event.category.id.in(categoriesIds);
            BooleanExpression byRangeStart = QEvent.event.eventDate.after(start);
            BooleanExpression byRangeEnd = QEvent.event.eventDate.before(end);
            foundEvents = eventRepository.findAll(byUserId.and(byStates).and(byCategoryId).and(byRangeStart).and(byRangeEnd), page);
        } else if (initiatorIds != null
                && states != null
                && categoriesIds != null) {
            BooleanExpression byUserId = QEvent.event.initiator.id.in(initiatorIds);
            BooleanExpression byStates = QEvent.event.state.in(listOfStates);
            BooleanExpression byCategoryId = QEvent.event.category.id.in(categoriesIds);
            foundEvents = eventRepository.findAll(byUserId.and(byStates).and(byCategoryId), page);
        } else if (initiatorIds != null
                && states != null) {
            BooleanExpression byUserId = QEvent.event.initiator.id.in(initiatorIds);
            BooleanExpression byStates = QEvent.event.state.in(listOfStates);
            foundEvents = eventRepository.findAll(byUserId.and(byStates), page);
        } else if (initiatorIds != null) {
            BooleanExpression byUserId = QEvent.event.initiator.id.in(initiatorIds);
            foundEvents = eventRepository.findAll(byUserId, page);
        } else {
            return eventRepository.findAll(page).stream()
                    .map(event -> EventMapper.mapToEventFullDto(event, 0))
                    .collect(Collectors.toList());

        }
        return StreamSupport.stream(foundEvents.spliterator(), false)
                .map(event -> EventMapper.mapToEventFullDto(event, 0))
                .collect(Collectors.toList());

    }

    @Override
    public List<EventFullDto> getEventByInitiator(Long initiatorId,
                                                  Integer from,
                                                  Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        return eventRepository.findAllEventByInitiatorId(initiatorId, page).stream()
                .map(event -> EventMapper.mapToEventFullDto(event, 1))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEventByParamPublic(String text,
                                                    List<Long> categories,
                                                    Boolean paid,
                                                    String rangeStart,
                                                    String rangeEnd,
                                                    Boolean onlyAvailable,
                                                    String sort,
                                                    Integer from,
                                                    Integer size,
                                                    HttpServletRequest request) {
        PageRequest page = PageRequest.of(from / size, size);
        List<Event> foundEvents = null;
        List<State> listOfStates = null;
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, formatter);
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, formatter);
            if (end.isBefore(LocalDateTime.now())) {
                throw new ValidationException("rangeEnd не может быть в прошлом");
            }
        }
        hitClient.createHit(new HitDto(
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(formatter)
        ));
        if (text != null
                && paid != null
                && rangeStart != null
                && rangeEnd != null
                && onlyAvailable != null
                && sort != null) {
            foundEvents = eventRepository.getEventsByAllParam(text, categories, paid, start, end, page);
            if (sort.equals("EVENT_DATE")) {
                return foundEvents.stream()
                        .map(event -> EventMapper.mapToEventFullDto(event, 1))
                        .sorted(Comparator.comparing(EventFullDto::getEventDate))
                        .collect(Collectors.toList());
            } else {
                return foundEvents.stream()
                        .map(event -> EventMapper.mapToEventFullDto(event, 1))
                        .sorted(Comparator.comparing(EventFullDto::getViews))
                        .collect(Collectors.toList());
            }
        } else if (text != null
                && paid != null
                && rangeStart != null
                && rangeEnd != null
                && onlyAvailable != null) {
            foundEvents = eventRepository.getEventsByTextCatPaid(text, categories, paid, page);
            return foundEvents.stream()
                    .map(event -> EventMapper.mapToEventFullDto(event, 1))
                    .collect(Collectors.toList());
        } else if (text != null
                && paid != null
                && rangeStart != null
                && rangeEnd != null) {
            foundEvents = eventRepository.getEventsByTextCat(text, categories, page);
            return foundEvents.stream()
                    .map(event -> EventMapper.mapToEventFullDto(event, 1))
                    .collect(Collectors.toList());
        } else if (text != null
                && paid != null) {
            foundEvents = eventRepository.getEventsByTextCat(text, categories, page);
            return foundEvents.stream()
                    .map(event -> EventMapper.mapToEventFullDto(event, 1))
                    .collect(Collectors.toList());
        } else if (text != null) {
            foundEvents = eventRepository.getEventsByText(text, page);
            return foundEvents.stream()
                    .map(event -> EventMapper.mapToEventFullDto(event, 1))
                    .collect(Collectors.toList());
        } else {
            return eventRepository.findAll(page).stream()
                    .map(event -> EventMapper.mapToEventFullDto(event, 1))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public EventFullDto getEventByIdPublish(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoDataException("События не существует"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NoDataException("События не существует");
        }
        hitClient.createHit(new HitDto(
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(formatter)
        ));
        return EventMapper.mapToEventFullDto(event, 1);
    }

    @Override
    public EventFullDto getEventByInitiator(Long userId, Long eventId) {
        return EventMapper.mapToEventFullDto(eventRepository.findByIdAndInitiatorId(eventId, userId), 5);
    }

    @Transactional
    @Override
    public EventFullDto updateEventByInitiator(UpdateEventUserRequest userEvent, Long userId, Long eventId) {
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NoDataException("Пользователя не существует"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoDataException("События не существует"));
        if (!event.getInitiator().equals(initiator)) {
            throw new AlreadyExistException("Пользователь не является инициатором выбранного события");
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new AlreadyExistException("Нельзя редактировать опубликованное событие");
        }
        if (userEvent.getAnnotation() != null) {
            event.setAnnotation(userEvent.getAnnotation());
        }
        if (userEvent.getDescription() != null) {
            event.setDescription(userEvent.getDescription());
        }
        if (userEvent.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(userEvent.getEventDate(), formatter);
            if (eventDate.isBefore(LocalDateTime.now())) {
                throw new ValidationException("Дата не может быть в прошлом");
            }
            event.setEventDate(eventDate);
        }
        if (userEvent.getLocation() != null) {
            Location location = locationRepository.save(LocationMapper.mapToLocation(userEvent.getLocation()));
            event.setLocation(location);
        }
        if (userEvent.getPaid() != null) {
            event.setPaid(userEvent.getPaid());
        }
        if (userEvent.getParticipantLimit() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неправильно задан параметр: лимит участников не может быть отрицательным");
        } else if (userEvent.getParticipantLimit() > 0) {
            event.setParticipantLimit(userEvent.getParticipantLimit());
        }
        if (userEvent.getRequestModeration() != null) {
            event.setRequestModeration(userEvent.getRequestModeration());
        }
        if (userEvent.getStateAction() != null) {
            event.setState(State.PENDING);
            if (userEvent.getStateAction().equals(State.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            }
        }
        if (userEvent.getTitle() != null) {
            event.setTitle(userEvent.getTitle());
        }
        return EventMapper.mapToEventFullDto(eventRepository.save(event), 5);
    }

}
