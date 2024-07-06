package ru.practicum.model.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    @Query(value = "select * from events e " +
            "join users u on e.initiator = u.id " +
            "join category c on e.category = c.id " +
            "where u.id in :initiatorIds " +
            "and e.state in :states " +
            "and c.id in :categoryIds " +
            "and e.event_date between :rangeStart " +
            "and :rangeEnd",
            nativeQuery = true)
    List<Event> getEvents(@Param("initiatorIds") List<Long> initiatorIds,
                          @Param("states") List<String> states,
                          @Param("categoryIds") List<Long> categoryIds,
                          @Param("rangeStart") LocalDateTime rangeStart,
                          @Param("rangeEnd") LocalDateTime rangeEnd,
                          Pageable pageable);

    @Query(value = "select * from events e " +
            "join category c on e.category = c.id " +
            "where lower(e.annotation) like lower(concat('%', :text, '%')) " +
            "and e.category in :categories " +
            "and e.paid = :paid " +
            "and e.event_date between :rangeStart and :rangeEnd",
            nativeQuery = true)
    List<Event> getEventsByAllParam(@Param("text") String text,
                                    @Param("categories") List<Long> categories,
                                    @Param("paid") Boolean paid,
                                    @Param("rangeStart") LocalDateTime rangeStart,
                                    @Param("rangeEnd") LocalDateTime rangeEnd,
                                    Pageable pageable);

    @Query(value = "select * from events e " +
            "join category c on e.category = c.id " +
            "where lower(e.annotation) like lower(concat('%', :text, '%')) " +
            "and e.category in :categories " +
            "and e.paid = :paid",
            nativeQuery = true)
    List<Event> getEventsByTextCatPaid(@Param("text") String text,
                                       @Param("categories") List<Long> categories,
                                       @Param("paid") Boolean paid,
                                       Pageable pageable);

    @Query(value = "select * from events e " +
            "join category c on e.category = c.id " +
            "where lower(e.annotation) like lower(concat('%', :text, '%')) " +
            "and e.category in :categories",
            nativeQuery = true)
    List<Event> getEventsByTextCat(@Param("text") String text,
                                   @Param("categories") List<Long> categories,
                                   Pageable pageable);

    @Query(value = "select * from events e " +
            "where lower(e.annotation) like lower(concat('%', :text, '%'))",
            nativeQuery = true)
    List<Event> getEventsByText(@Param("text") String text, Pageable pageable);

    List<Event> findAllEventByInitiatorId(Long initiatorId, Pageable pageable);

    Event findByIdAndInitiatorId(Long eventId, Long initiatorId);
}
