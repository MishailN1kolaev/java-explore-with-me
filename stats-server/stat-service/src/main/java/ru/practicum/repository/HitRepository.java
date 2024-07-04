package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Hit;
import ru.practicum.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query("select new ru.practicum.ViewStats(s.app, s.uri, count(s.uri)) " +
            "from Hit s where s.timestamp between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(s.uri) desc")
    List<ViewStats> getHits(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.ViewStats(s.app, s.uri, count(s.uri)) " +
            "from Hit s where s.timestamp between ?1 and ?2 and s.uri in ?3 " +
            "group by s.app, s.uri " +
            "order by count(s.uri) desc")
    List<ViewStats> getHitsUris(LocalDateTime start, LocalDateTime end, List<String> uris);


    @Query("select new ru.practicum.ViewStats(s.app, s.uri, count(distinct s.ip)) " +
            "from Hit s where s.timestamp between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(s.uri) desc")
    List<ViewStats> getHitsUnique(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.ViewStats(s.app, s.uri, count(distinct s.ip)) " +
            "from Hit s where s.timestamp between ?1 and ?2 and s.uri in ?3 " +
            "group by s.app, s.uri " +
            "order by count(s.uri) desc")
    List<ViewStats> getHitsUrisUnique(LocalDateTime start, LocalDateTime end, List<String> uris);


}
