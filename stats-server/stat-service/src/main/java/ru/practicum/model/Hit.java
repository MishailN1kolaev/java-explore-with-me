package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stats")
@Getter
@Setter
@ToString
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "app")
    private String app;

    @Column(name = "uri")
    private String uri;

    @Column(name = "ip")
    private String ip;

    @Column(name = "created")
    private LocalDateTime timestamp;
}
