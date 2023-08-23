package ru.practicum.ewm.mapper;

import ru.practicum.ewm.models.comments.Comment;
import ru.practicum.ewm.models.comments.CommentDto;
import ru.practicum.ewm.models.comments.NewCommentDto;
import ru.practicum.ewm.models.event.Event;
import ru.practicum.ewm.models.user.User;

public class CommentMapper {

    public static Comment newDtoToComment(NewCommentDto dto, User user, Event event) {
        return Comment.create(0L, user, event, dto.getText(), dto.getCreated(), dto.getImpression());
    }

    public static CommentDto commentToDto(Comment comment) {
        return CommentDto.create(comment.getUser().getId(), comment.getEvent().getId(), comment.getText(),
                comment.getCreated(), comment.getImpression());
    }

}
