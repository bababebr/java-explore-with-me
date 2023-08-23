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
        addCommentValidation(user, event, dto);
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

    private void addCommentValidation(User user, Event event, NewCommentDto dto) {
        Optional<ParticipantRequest> request = participantRepository.findByUserIdAndEventId(user.getId(), event.getId());

        if (!(request.isPresent() || event.getInitiator().getId().equals(user.getId()))) {
            throw new IllegalStateException(String.format("USER with ID=%s not a participant or owner of Event with ID=%s.",
                    user.getId(), user.getId()));
        }

    }
}
