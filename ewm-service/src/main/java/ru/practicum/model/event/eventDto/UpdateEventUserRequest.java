package ru.practicum.model.event.eventDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.event.State;
import ru.practicum.model.location.LocationDto;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest {

    @Size(min = 20)
    @Size(max = 2000)
    private String annotation;

    private int category;

    @Size(min = 20)
    @Size(max = 7000)
    private String description;

    private String eventDate;

    private LocationDto location;

    private Boolean paid;

    private int participantLimit;

    private Boolean requestModeration;

    private State stateAction;

    @Size(min = 3)
    @Size(max = 120)
    private String title;
}
