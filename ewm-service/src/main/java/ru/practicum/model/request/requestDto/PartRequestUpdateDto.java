package ru.practicum.model.request.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartRequestUpdateDto {

    private List<Long> requestIds;

    private String status;


}
