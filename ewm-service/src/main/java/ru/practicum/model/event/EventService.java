package ru.practicum.model.event;

import ru.practicum.model.event.eventDto.EventFullDto;
import ru.practicum.model.event.eventDto.NewEventDto;
import ru.practicum.model.event.eventDto.UpdateEventAdminRequest;
import ru.practicum.model.event.eventDto.UpdateEventUserRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {


    EventFullDto addEvent(NewEventDto newEventDto, Long userId, String path);


    EventFullDto updateEventByAdmin(UpdateEventAdminRequest adminEvent, Long eventId);

    List<EventFullDto> getEventsByParam(List<Long> initiatorIds,
                                        List<String> states,
                                        List<Long> categoriesIds,
                                        String rangeStart,
                                        String rangeEnd,
                                        Integer from,
                                        Integer size);

    List<EventFullDto> getEventByInitiator(Long initiatorId,
                                           Integer from,
                                           Integer size);

    List<EventFullDto> getEventByParamPublic(String text,
                                             List<Long> categories,
                                             Boolean paid,
                                             String rangeStart,
                                             String rangeEnd,
                                             Boolean onlyAvailable,
                                             String sort,
                                             Integer from,
                                             Integer size,
                                             HttpServletRequest request);

    EventFullDto getEventByIdPublish(Long eventId, HttpServletRequest request);

    EventFullDto getEventByInitiator(Long userId, Long eventId);

    EventFullDto updateEventByInitiator(UpdateEventUserRequest userEvent, Long userId, Long eventId);
}
