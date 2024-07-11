package ru.practicum.model.comment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.comment.commentDto.CommentDto;
import ru.practicum.model.comment.commentDto.NewCommentDto;
import ru.practicum.model.event.Event;
import ru.practicum.model.user.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    public static Comment mapToComment(NewCommentDto newComment, Event event, User author) {
        Comment comment = new Comment();
        comment.setCreated(LocalDateTime.now());
        comment.setEvent(event);
        comment.setAuthor(author);
        comment.setText(newComment.getText());
        return comment;
    }

    public static CommentDto mapToCommentDto(Comment comment) {

        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

}