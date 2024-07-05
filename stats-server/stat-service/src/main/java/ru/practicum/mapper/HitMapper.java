package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.HitDto;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HitMapper {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Hit mapToHit(HitDto hitDto) {
        Hit hit = new Hit();
        hit.setApp(hitDto.getApp());
        hit.setUri(hitDto.getUri());
        hit.setIp(hitDto.getIp());
        hit.setTimestamp(LocalDateTime.parse(hitDto.getTimestamp(), formatter));
        return hit;
    }

    public static HitDto mapToHitDto(Hit hit) {
        return new HitDto(
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp().toString()
        );
    }


}
