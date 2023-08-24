package ru.practicum.ewm.service.interfaces;

import ru.practicum.ewm.models.comments.Comment;
import ru.practicum.ewm.models.comments.CommentDto;
import ru.practicum.ewm.models.comments.NewCommentDto;

import java.util.List;

public interface ICommentService {

    CommentDto add(Long userId, Long eventId, NewCommentDto dto);

    CommentDto update(Long userId, Long eventId, NewCommentDto dto);

    List<CommentDto> getEventComments(Long eventId);

    List<CommentDto> getUserComments(Long userId);

    CommentDto getById(Long commentId);

    CommentDto delete(Long userId, Long commentId);

}
