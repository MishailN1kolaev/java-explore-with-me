package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.HitDto;
import ru.practicum.Utils;
import ru.practicum.ViewStats;
import ru.practicum.mapper.HitMapper;
import ru.practicum.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {
    private final HitRepository repository;

    @Transactional
    @Override
    public HitDto createHit(HitDto hitDto) {
        return HitMapper.mapToHitDto(repository.save(HitMapper.mapToHit(hitDto)));
    }

    @Override
    public List<ViewStats> getHit(String startSt, String endSt, List<String> uris, Boolean unique) {
        LocalDateTime start = LocalDateTime.parse(startSt, Utils.DATE_FORMATTER);
        LocalDateTime end = LocalDateTime.parse(endSt, Utils.DATE_FORMATTER);
        if (start.isAfter(end)) {
            throw new ru.practicum.exeption.ValidationException("Неверно заданы границы диапазона");
        }
        if (!unique) {
            if (uris == null) {
                return repository.getHits(start, end);
            } else {
                return repository.getHitsUris(start, end, uris);
            }
        } else {
            if (uris == null) {
                return repository.getHitsUnique(start, end);
            } else {
                return repository.getHitsUrisUnique(start, end, uris);

            }
        }
    }
}
