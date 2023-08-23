package ru.practicum.ewm.service.interfaces;

import ru.practicum.ewm.models.comments.Comment;
import ru.practicum.ewm.models.comments.CommentDto;
import ru.practicum.ewm.models.comments.NewCommentDto;

import java.util.List;

public interface ICommentService {

    CommentDto add(Long userId, Long eventId, NewCommentDto dto);

    CommentDto update(CommentDto dto);

    List<CommentDto> getEventComments(Long eventId);

    List<CommentDto> getUserComments(Long userId);

    Comment getById(Long commentId);

    CommentDto delete(Long commentId);

}
