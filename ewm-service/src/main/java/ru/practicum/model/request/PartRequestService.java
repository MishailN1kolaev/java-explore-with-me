package ru.practicum.model.request;

import ru.practicum.model.request.requestDto.EventRequestStatusUpdateResult;
import ru.practicum.model.request.requestDto.PartRequestUpdateDto;
import ru.practicum.model.request.requestDto.ParticipationRequestDto;

import java.util.List;

public interface PartRequestService {

    List<ParticipationRequestDto> getPartRequest(Long userId);

    ParticipationRequestDto addPartRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelPartRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getPartRequestByUserAndEvent(Long userId, Long eventId);

    EventRequestStatusUpdateResult updatePartRequest(Long userId, Long eventId, PartRequestUpdateDto userRequest);
}
