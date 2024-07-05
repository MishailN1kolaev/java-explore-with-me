package ru.practicum.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.request.requestDto.ParticipationRequestDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {

    private ParticipationRequestDto confirmedRequests;

    private ParticipationRequestDto rejectedRequests;

}
