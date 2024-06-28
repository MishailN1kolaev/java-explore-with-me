package ru.practicum;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatController {
    private final HitClient hitClient;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(Utils.HIT)
    public ResponseEntity<Object> createHit(@RequestBody HitDto requestDto) {
        log.info("Creating hit {}", requestDto);
        return hitClient.createHit(requestDto);
    }

    @GetMapping(Utils.STATS)
    public ResponseEntity<Object> getHits(@NonNull @RequestParam LocalDateTime startDate,
                                          @NonNull @RequestParam LocalDateTime endDate,
                                          @RequestParam List<String> uris,
                                          @RequestParam(defaultValue = "false") Boolean unique
    ) {
        return hitClient.getHit(startDate, endDate, uris, unique);
    }


}
