package ru.practicum.ewm.models.compilations;

import lombok.*;
import ru.practicum.ewm.models.event.Event;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "compilation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToMany
    @JoinTable(
            name = "compilation_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    Set<Event> event;
    Boolean pinned;
    String title;
}
