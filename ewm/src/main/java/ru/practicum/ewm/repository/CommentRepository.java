package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.models.comments.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByUserIdAndEventId(Long userId, Long eventId);

    List<Comment> findAllByEventId(Long eventId);

    List<Comment> findByUserId(Long userId);

}
