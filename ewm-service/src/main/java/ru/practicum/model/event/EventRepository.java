package ru.practicum.model.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {


    @Query(value = "select * from events e " +
            "join users u on e.initiator = u.id " +
            "join category c on e.category = c.id " +
            "where u.id in ?1 and e.state in ?2 and c.id in ?3 and e.event_date between ?4 and ?5",
            nativeQuery = true)
    List<Event> getEvents(List<Long> initiatorIds,
                          List<String> states,
                          List<Long> categoriesIds,
                          LocalDateTime rangeStart,
                          LocalDateTime rangeEnd,
                          Pageable pageable);

    @Query(value = "select * from events e " +
            "join category c on e.category = c.id " +
            "where lower(e.annotation) like lower(concat('%', ?1, '%')) " +
            "and e.category in ?2 " +
            "and e.paid = ?3 " +
            "and e.event_date between ?4 and ?5",
            nativeQuery = true)
    List<Event> getEventsByAllParam(String text,
                                    List<Long> categories,
                                    Boolean paid,
                                    LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd,
                                    Pageable pageable);

    @Query(value = "select * from events e " +
            "join category c on e.category = c.id " +
            "where lower(e.annotation) like lower(concat('%', ?1, '%')) " +
            "and e.category in ?2 " +
            "and e.paid = ?3 ",
            nativeQuery = true)
    List<Event> getEventsByTextCatPaid(String text,
                                       List<Long> categories,
                                       Boolean paid,
                                       Pageable pageable);

    @Query(value = "select * from events e " +
            "join category c on e.category = c.id " +
            "where lower(e.annotation) like lower(concat('%', ?1, '%')) " +
            "and e.category in ?2 ",
            nativeQuery = true)
    List<Event> getEventsByTextCat(String text,
                                   List<Long> categories,
                                   Pageable pageable);

    @Query(value = "select * from events e " +
            "where lower(e.annotation) like lower(concat('%', ?1, '%')) ",
            nativeQuery = true)
    List<Event> getEventsByText(String text, Pageable pageable);

    List<Event> findAllEventByInitiatorId(Long initiatorId, Pageable pageable);

    Event findByIdAndInitiatorId(Long eventId, Long initiatorId);


}

