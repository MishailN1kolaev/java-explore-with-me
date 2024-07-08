package ru.practicum.model.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventRepository;
import ru.practicum.model.event.State;
import ru.practicum.model.event.Status;
import ru.practicum.model.exeption.AlreadyExistException;
import ru.practicum.model.exeption.NoDataException;
import ru.practicum.model.exeption.ValidationException;
import ru.practicum.model.request.requestDto.EventRequestStatusUpdateResult;
import ru.practicum.model.request.requestDto.PartRequestUpdateDto;
import ru.practicum.model.request.requestDto.ParticipationRequestDto;
import ru.practicum.model.user.User;
import ru.practicum.model.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PartRequestServiceImpl implements PartRequestService {
    private final PartRequestRepository partRequestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;


    @Override
    public List<ParticipationRequestDto> getPartRequest(Long userId) {
        return partRequestRepository.findByRequesterId(userId).stream()
                .map(PartRequestMapper::mapToPartRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto addPartRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoDataException("Пользователя не существует"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoDataException("События не существует"));
        if (partRequestRepository.findAll().stream()
                .anyMatch(partRequest -> partRequest.getEvent().equals(event) && partRequest.getRequester().equals(user))) {
            throw new AlreadyExistException("Пользователь уже отправил заявку на участие");
        }
        if (event.getInitiator().equals(user)) {
            throw new AlreadyExistException("Пользователь не может отправить заявку на участие в событии, где является организатором");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new AlreadyExistException("События не существует");
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new AlreadyExistException("В событии нет свободных мест");
        }
        if (!event.getRequestModeration()) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }

        return PartRequestMapper.mapToPartRequestDto(partRequestRepository.save(
                PartRequestMapper.mapToPartRequest(event, user)));
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelPartRequest(Long userId, Long requestId) {
        PartRequest partRequest = partRequestRepository.findById(requestId).orElseThrow(() -> new NoDataException("Запрос не существует"));
        if (!partRequest.getRequester().getId().equals(userId)) {
            throw new ValidationException("Пользователь не создавал указанный запрос");
        }
        partRequest.setStatus(Status.CANCELED);
        partRequestRepository.deleteById(requestId);
        return PartRequestMapper.mapToPartRequestDto(partRequest);
    }

    @Override
    public List<ParticipationRequestDto> getPartRequestByUserAndEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoDataException("Пользователя не существует"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoDataException("События не существует"));
        if (!event.getInitiator().equals(user)) {
            throw new ValidationException("Пользователь не является инициатором выбранного события");
        }
        return partRequestRepository.findByEventId(eventId).stream()
                .map(PartRequestMapper::mapToPartRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult updatePartRequest(Long userId, Long eventId, PartRequestUpdateDto userRequest) {

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoDataException("События не существует"));
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new AlreadyExistException("В событии нет мест");
        }

        List<PartRequest> requests = partRequestRepository.findAllById(userRequest.getRequestIds());
        Status status;
        try {
            status = Status.valueOf(userRequest.getStatus());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        for (PartRequest request : requests) {
            if (request.getStatus().equals(Status.CONFIRMED)) {
                throw new AlreadyExistException("Невозможно подтвердить заявку");
            }
            request.setStatus(status);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }

        List<ParticipationRequestDto> confirmedRequests = partRequestRepository.findByStatusOrderByIdDesc(Status.CONFIRMED).stream()
                .map(PartRequestMapper::mapToPartRequestDto)
                .collect(Collectors.toList());
        List<ParticipationRequestDto> rejectedRequests = partRequestRepository.findByStatusOrderByIdDesc(Status.REJECTED).stream()
                .map(PartRequestMapper::mapToPartRequestDto)
                .collect(Collectors.toList());
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

}
