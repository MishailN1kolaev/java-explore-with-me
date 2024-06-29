package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.HitDto;
import ru.practicum.HitStatsDto;
import ru.practicum.Utils;
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
    public List<HitStatsDto> getHit(String startSt, String endSt, List<String> uris, Boolean unique) {
        LocalDateTime start = LocalDateTime.parse(startSt, Utils.DATE_FORMATTER);
        LocalDateTime end = LocalDateTime.parse(endSt, Utils.DATE_FORMATTER);
        return repository.getHitStats(start, end, uris, unique);
    }
}