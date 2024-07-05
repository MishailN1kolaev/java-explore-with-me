package ru.practicum.model.compilation.compilationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationDto {

    private List<Long> events;

    private Boolean pinned = false;

    @Size(min = 1)
    @Size(max = 50)
    private String title;

}
