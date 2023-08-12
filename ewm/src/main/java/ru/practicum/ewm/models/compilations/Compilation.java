package ru.practicum.ewm.models.compilations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.models.event.Event;

import javax.persistence.*;

@Entity
@Table(name = "compilation")
@Getter
@Setter
@RequiredArgsConstructor(staticName = "create")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "compilation_id")
    Long compilationId;
    @ManyToOne
    @JoinColumn(name = "event")
    Event event;
    Boolean pinned;
    String title;
}
