package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class ViewStats {

    private String app;

    private String uri;

    private Long hits;
}
