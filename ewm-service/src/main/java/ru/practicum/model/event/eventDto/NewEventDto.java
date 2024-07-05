package ru.practicum.model.event.eventDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.practicum.model.location.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @Size(min = 20)
    @Size(max = 2000)
    @NonNull
    @NotBlank
    private String annotation;


    private Long category;

    @Size(min = 20)
    @Size(max = 7000)
    @NonNull
    @NotBlank
    private String description;

    @NonNull
    private String eventDate;

    private LocationDto location;

    private Boolean paid = false;

    private int participantLimit = 0;

    private Boolean requestModeration = true;

    @Size(min = 3)
    @Size(max = 120)
    @NonNull
    @NotBlank
    private String title;

}
