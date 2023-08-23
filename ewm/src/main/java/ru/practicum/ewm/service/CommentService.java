package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.mapper.CommentMapper;
import ru.practicum.ewm.models.comments.Comment;
import ru.practicum.ewm.models.comments.CommentDto;
import ru.practicum.ewm.models.comments.NewCommentDto;
import ru.practicum.ewm.models.event.Event;
import ru.practicum.ewm.models.user.User;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.interfaces.ICommentService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CommentService implements ICommentService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(UserRepository userRepository, EventRepository eventRepository,
                          CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentDto add(Long userId, Long eventId, NewCommentDto dto) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new NoSuchElementException(String.format("User with ID=%s not found", userId)));
        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new NoSuchElementException(String.format("Event with ID=%s not found", eventId)));
        Comment c = commentRepository.save(CommentMapper.newDtoToComment(dto, user, event));
        return CommentMapper.commentToDto(c);
    }

    @Override
    public CommentDto update(CommentDto dto) {
        return null;
    }

    @Override
    public List<CommentDto> getEventComments(Long eventId) {
        return null;
    }

    @Override
    public List<CommentDto> getUserComments(Long userId) {
        return null;
    }

    @Override
    public Comment getById(Long commentId) {
        return null;
    }

    @Override
    public CommentDto delete(Long commentId) {
        return null;
    }
}
