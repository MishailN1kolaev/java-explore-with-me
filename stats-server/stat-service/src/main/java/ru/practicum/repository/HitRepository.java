package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ViewStats;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query("select new ru.practicum.ViewStats(s.app, s.uri, count(s.uri)) " +
            "from Hit s where s.timestamp between :start and :end " +
            "group by s.app, s.uri " +
            "order by count(s.uri) desc")
    List<ViewStats> getHits(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select new ru.practicum.ViewStats(s.app, s.uri, count(s.uri)) " +
            "from Hit s where s.timestamp between :start and :end and s.uri in :uris " +
            "group by s.app, s.uri " +
            "order by count(s.uri) desc")
    List<ViewStats> getHitsUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

    @Query("select new ru.practicum.ViewStats(s.app, s.uri, count(distinct s.ip)) " +
            "from Hit s where s.timestamp between :start and :end " +
            "group by s.app, s.uri " +
            "order by count(s.uri) desc")
    List<ViewStats> getHitsUnique(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select new ru.practicum.ViewStats(s.app, s.uri, count(distinct s.ip)) " +
            "from Hit s where s.timestamp between :start and :end and s.uri in :uris " +
            "group by s.app, s.uri " +
            "order by count(s.uri) desc")
    List<ViewStats> getHitsUrisUnique(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uris") List<String> uris);
}