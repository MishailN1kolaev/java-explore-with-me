package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.event.EventServiceImpl;
import ru.practicum.model.event.eventDto.EventFullDto;
import ru.practicum.model.event.eventDto.NewEventDto;
import ru.practicum.model.event.eventDto.UpdateEventUserRequest;
import ru.practicum.model.request.PartRequestService;
import ru.practicum.model.request.requestDto.EventRequestStatusUpdateResult;
import ru.practicum.model.request.requestDto.PartRequestUpdateDto;
import ru.practicum.model.request.requestDto.ParticipationRequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
@Validated
public class PrivateController {
    private final EventServiceImpl eventService;
    private final PartRequestService partRequestService;

    @GetMapping("/events")
    public List<EventFullDto> getEventsByUser(@PathVariable Long userId,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getEventByInitiator(userId, from, size);
    }

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @RequestBody @Valid NewEventDto newEventDto
    ) {
        return eventService.addEvent(newEventDto, userId, "path");
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getEventFullByUser(@PathVariable Long userId,
                                           @PathVariable Long eventId) {
        return eventService.getEventByInitiator(userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEventByUser(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          @Valid @RequestBody UpdateEventUserRequest userRequest) {
        return eventService.updateEventByInitiator(userRequest, userId, eventId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForEvent(@PathVariable Long userId,
                                                             @PathVariable Long eventId) {
        return partRequestService.getPartRequestByUserAndEvent(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestByUser(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @RequestBody(required = false) PartRequestUpdateDto userRequest) {
        return partRequestService.updatePartRequest(userId, eventId, userRequest);
    }

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getRequestsByUser(@PathVariable Long userId) {
        return partRequestService.getPartRequest(userId);
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequestByUser(@PathVariable Long userId,
                                                    @RequestParam Long eventId) {
        return partRequestService.addPartRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestByUser(@PathVariable Long userId,
                                                       @PathVariable Long requestId) {
        return partRequestService.cancelPartRequest(userId, requestId);
    }


}
