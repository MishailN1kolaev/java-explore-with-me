package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.HitStatsDto;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query("select new ru.practicum.HitStatsDto(s.app, s.uri, count(s.uri)) " +
            "from Hit s where s.timestamp between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(s.uri) desc")
    List<HitStatsDto> getHits(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select new ru.practicum.HitStatsDto(s.app, s.uri, count(s.uri)) " +
            "from Hit s where s.timestamp between ?1 and ?2 and s.uri in ?3 " +
            "group by s.app, s.uri " +
            "order by count(s.uri) desc")
    List<HitStatsDto> getHitsUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);


    @Query("select new ru.practicum.HitStatsDto(s.app, s.uri, count(distinct s.ip)) " +
            "from Hit s where s.timestamp between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(s.uri) desc")
    List<HitStatsDto> getHitsUnique(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select new ru.practicum.HitStatsDto(s.app, s.uri, count(distinct s.ip)) " +
            "from Hit s where s.timestamp between ?1 and ?2 and s.uri in ?3 " +
            "group by s.app, s.uri " +
            "order by count(s.uri) desc")
    List<HitStatsDto> getHitsUrisUnique(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

}