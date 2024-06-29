package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.HitStatsDto;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query("select new ru.practicum.HitStatsDto(s.app, s.uri, " +
            "count(case when :unique = true then distinct s.ip else s.uri end)) " +
            "from Hit s " +
            "where (:start is null or s.timestamp >= :start) " +
            "and (:end is null or s.timestamp <= :end) " +
            "and (:uris is null or s.uri in :uris) " +
            "group by s.app, s.uri " +
            "order by count(s.uri) desc")
    List<HitStatsDto> getHits(@Param("start") LocalDateTime start,
                              @Param("end") LocalDateTime end,
                              @Param("uris") List<String> uris,
                              @Param("unique") Boolean unique);

    @Query("select new ru.practicum.HitStatsDto(s.app, s.uri, count(s.uri)) " +
            "from Hit s where s.timestamp between :start and :end and s.uri in :uris " +
            "group by s.app, s.uri " +
            "order by count(s.uri) desc")
    List<HitStatsDto> getHitsUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

    @Query("select new ru.practicum.HitStatsDto(s.app, s.uri, count(distinct s.ip)) " +
            "from Hit s where s.timestamp between :start and :end " +
            "group by s.app, s.uri " +
            "order by count(s.uri) desc")
    List<HitStatsDto> getHitsUnique(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select new ru.practicum.HitStatsDto(s.app, s.uri, count(distinct s.ip)) " +
            "from Hit s where s.timestamp between :start and :end and s.uri in :uris " +
            "group by s.app, s.uri " +
            "order by count(s.uri) desc")
    List<HitStatsDto> getHitsUrisUnique(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

}
