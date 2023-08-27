package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.enums.EventStatus;
import ru.practicum.ewm.mapper.CommentMapper;
import ru.practicum.ewm.models.comments.Comment;
import ru.practicum.ewm.models.comments.CommentDto;
import ru.practicum.ewm.models.comments.NewCommentDto;
import ru.practicum.ewm.models.comments.UpdateCommentDto;
import ru.practicum.ewm.models.event.Event;
import ru.practicum.ewm.models.user.User;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipantRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.interfaces.ICommentService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CommentService implements ICommentService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final ParticipantRepository participantRepository;


    @Autowired
    public CommentService(UserRepository userRepository, EventRepository eventRepository,
                          CommentRepository commentRepository, ParticipantRepository participantRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.commentRepository = commentRepository;
        this.participantRepository = participantRepository;
    }

    @Override
    public CommentDto add(Long userId, Long eventId, NewCommentDto dto) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new NoSuchElementException(String.format("User with ID=%s not found", userId)));
        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new NoSuchElementException(String.format("Event with ID=%s not found", eventId)));
        addCommentValidation(user, event);
        Comment c = commentRepository.save(CommentMapper.newDtoToComment(dto, user, event));
        return CommentMapper.commentToDto(c);
    }

    @Override
    public CommentDto update(Long userId, UpdateCommentDto dto) {
        Comment comment = commentRepository.findById(dto.getId()).orElseThrow(()
                -> new NoSuchElementException(String
                .format("Comment with ID=%s not found", dto.getId())));
        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Its not your comment.");
        }
        if (!comment.getText().equals(dto.getText())) {
            comment.setText(dto.getText());
            comment.setUpdated(dto.getUpdated());
        } else {
            throw new IllegalArgumentException("Update with the same text is prohibited.");
        }
        return CommentMapper.commentToDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto updateByAdmin(UpdateCommentDto dto) {
        Comment comment = commentRepository.findById(dto.getId()).orElseThrow(()
                -> new NoSuchElementException(String
                .format("Comment with ID=%s not found", dto.getId())));
        if (!comment.getText().equals(dto.getText())) {
            comment.setText(dto.getText());
            comment.setUpdated(dto.getUpdated());
        } else {
            throw new IllegalArgumentException("Update with the same text is prohibited.");
        }
        return CommentMapper.commentToDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getEventComments(Long eventId) {
        List<Comment> comments = commentRepository.findAllByEventId(eventId);
        return comments.stream().map(CommentMapper::commentToDto).collect(Collectors.toList());

    }

    @Override
    public List<CommentDto> getUserComments(Long userId, Long eventId) {
        List<Comment> comments = commentRepository.findAllByUserIdAndEventId(userId, eventId);
        return comments.stream().map(CommentMapper::commentToDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto getUserComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findAllByIdAndUserId(commentId, userId).orElseThrow(() ->
                new NoSuchElementException(String.format("User ID=%s don't have Comment with ID=%s", userId, commentId)));
        return CommentMapper.commentToDto(comment);
    }

    @Override
    public CommentDto delete(Long userId, Long commentId) {
        Comment comment = commentRepository.findAllByIdAndUserId(commentId, userId).orElseThrow(() ->
                new NoSuchElementException(String.format("Can't delete comment. Comment ID=%s not exist", commentId)));
        commentRepository.delete(comment);
        return CommentMapper.commentToDto(comment);
    }

    @Override
    public CommentDto deleteByAdmin(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException(
                String.format("No comment with ID=%s", commentId)));
        commentRepository.delete(comment);
        return CommentMapper.commentToDto(comment);
    }

    private void addCommentValidation(User user, Event event) {
        if (commentRepository.findByUserIdAndEventId(user.getId(), event.getId()).isPresent()) {
            throw new IllegalStateException(String.format("Event ID=%s already has comment form User ID=%s",
                    event.getId(), user.getId()));
        }
        if (!event.getState().equals(EventStatus.PUBLISHED)) {
            throw new IllegalStateException("Event not published yet.");
        }
    }
}
