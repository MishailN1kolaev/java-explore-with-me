package ru.practicum.model.request;

import ru.practicum.model.event.Event;
import ru.practicum.model.event.Status;
import ru.practicum.model.request.requestDto.ParticipationRequestDto;
import ru.practicum.model.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PartRequestMapper {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static PartRequest mapToPartRequest(Event event,
                                               User user) {
        PartRequest partRequest = new PartRequest();
        partRequest.setRequester(user);
        partRequest.setCreated(LocalDateTime.now());
        partRequest.setEvent(event);
        if (event.getParticipantLimit() == 0) {
            partRequest.setStatus(Status.CONFIRMED);
        }
        return partRequest;
    }

    public static ParticipationRequestDto mapToPartRequestDto(PartRequest partRequest) {
        return new ParticipationRequestDto(
                partRequest.getId(),
                partRequest.getCreated().format(formatter),
                partRequest.getEvent().getId(),
                partRequest.getEvent().getId(),
                partRequest.getStatus()
        );
    }

}
