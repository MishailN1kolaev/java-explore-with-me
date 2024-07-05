package ru.practicum.model.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.category.Category;
import ru.practicum.model.location.Location;
import ru.practicum.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "annotation")
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    private Category category;

    @Column(name = "confirmed_requests")
    private int confirmedRequests;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "description")
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator")
    private User initiator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location")
    private Location location;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "participant_limit")
    private int participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state = State.PENDING;

    @Column(name = "title")
    private String title;


}
