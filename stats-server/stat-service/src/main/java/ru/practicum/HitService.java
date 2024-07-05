package ru.practicum;

import java.util.List;

public interface HitService {

    HitDto createHit(HitDto hitDto);

    List<ViewStats> getHit(String start, String end, List<String> uris, Boolean unique);
}
