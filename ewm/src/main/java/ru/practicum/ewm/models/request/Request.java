package ru.practicum.ewm.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.enums.State;
import ru.practicum.ewm.models.event.Event;
import ru.practicum.ewm.models.user.User;

import javax.persistence.*;

@Entity
@Table(name = "request")
@Getter
@Setter
@AllArgsConstructor(staticName = "create")
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;
    @Enumerated(EnumType.STRING)
    State state;

}
