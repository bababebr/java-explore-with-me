package ru.practicum.ewm.models.comments;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.models.event.Event;
import ru.practicum.ewm.models.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor(staticName = "create")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;
    String text;
    LocalDateTime created;
    LocalDateTime updated;

}
