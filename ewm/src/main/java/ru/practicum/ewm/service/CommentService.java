package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.mapper.CommentMapper;
import ru.practicum.ewm.models.comments.Comment;
import ru.practicum.ewm.models.comments.CommentDto;
import ru.practicum.ewm.models.comments.NewCommentDto;
import ru.practicum.ewm.models.event.Event;
import ru.practicum.ewm.models.participantRequest.ParticipantRequest;
import ru.practicum.ewm.models.user.User;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipantRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.interfaces.ICommentService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
    public CommentDto update(Long userId, Long eventId, NewCommentDto dto) {
        Comment comment = commentRepository.findByUserIdAndEventId(userId, eventId).orElseThrow(()
                -> new NoSuchElementException(String
                .format("Event with ID=%S don't have comment form User ID=%s", eventId, userId)));
        if (!comment.getText().equals(dto.getText())) {
            comment.setText(dto.getText());
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
    public List<CommentDto> getUserComments(Long userId) {
        List<Comment> comments = commentRepository.findByUserId(userId);
        return comments.stream().map(CommentMapper::commentToDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto getById(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException(
                String.format("No comment with ID=%s", commentId)));
        return CommentMapper.commentToDto(comment);
    }

    @Override
    public CommentDto delete(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException(
                String.format("No comment with ID=%s", commentId)));
        if (comment.getUser().getId() == userId || comment.getEvent().getInitiator().getId() == userId) {
            commentRepository.delete(comment);
            return CommentMapper.commentToDto(comment);
        } else {
            throw new IllegalStateException("Comment can be deleted by owner or admin");
        }
    }

    private void addCommentValidation(User user, Event event) {
        Optional<ParticipantRequest> request = participantRepository.findByUserIdAndEventId(user.getId(), event.getId());
        if (commentRepository.findByUserIdAndEventId(user.getId(), event.getId()).isPresent()) {
            throw new IllegalStateException(String.format("Event ID=%s already has comment form User ID=%s",
                    event.getId(), user.getId()));
        }
        if (!(request.isPresent() || event.getInitiator().getId().equals(user.getId()))) {
            throw new IllegalStateException(String.format("USER with ID=%s not a participant or owner of Event with ID=%s.",
                    user.getId(), event.getId()));
        }

    }
}
