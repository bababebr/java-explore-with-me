package ru.practicum.ewm.models.location;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
@ToString
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "event_id")
    Long eventId;
    Double lat;
    Double lon;
}
