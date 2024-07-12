package ru.practicum.model.event;

import ru.practicum.model.category.Category;
import ru.practicum.model.category.CategoryMapper;
import ru.practicum.model.comment.commentDto.CommentDto;
import ru.practicum.model.event.eventDto.EventFullDto;
import ru.practicum.model.event.eventDto.EventShortDto;
import ru.practicum.model.event.eventDto.NewEventDto;
import ru.practicum.model.exeption.ValidationException;
import ru.practicum.model.location.Location;
import ru.practicum.model.location.LocationMapper;
import ru.practicum.model.user.User;
import ru.practicum.model.user.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventMapper {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event mapToEvent(NewEventDto eventDto, Category category, User initiator, Location location) {
        Event event = new Event();
        event.setAnnotation(eventDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(eventDto.getDescription());
        LocalDateTime eventDate = LocalDateTime.parse(eventDto.getEventDate(), formatter);
        if (eventDate.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Дата не может быть в прошлом");
        }
        event.setEventDate(eventDate);
        event.setLocation(location);
        event.setPaid(eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        event.setRequestModeration(eventDto.getRequestModeration());
        event.setTitle(eventDto.getTitle());
        event.setCreated(LocalDateTime.now());
        event.setInitiator(initiator);
        return event;
    }


    public static EventFullDto mapToEventFullDto(Event event, int views, List<CommentDto> commentDtoList) {
        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.mapToCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreated().format(formatter),
                event.getDescription(),
                event.getEventDate().format(formatter),
                UserMapper.mapToUserShortDto(event.getInitiator()),
                LocationMapper.mapToLocationDto(event.getLocation()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                views,
                commentDtoList
        );
    }

    public static EventShortDto mapToEventShortDto(Event event, int views) {
        return new EventShortDto(
                event.getAnnotation(),
                CategoryMapper.mapToCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate().format(formatter),
                event.getId(),
                UserMapper.mapToUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                views
        );
    }

}
