package ru.practicum.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.HitDto;
import ru.practicum.service.HitService;
import ru.practicum.ViewStats;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class Controller {
    private final HitService hitService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hit")
    public HitDto makeHit(@RequestBody HitDto hitDto) {
        return hitService.createHit(hitDto);
    }

    @GetMapping("/stats")
    public List<ViewStats> getHits(@NonNull @RequestParam String start,
                                   @NonNull @RequestParam String end,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(defaultValue = "false") Boolean unique
    ) {
        return hitService.getHit(start, end, uris, unique);
    }


}
