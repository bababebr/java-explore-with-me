package ru.practicum.ewm.models.participantRequest;

import lombok.*;
import ru.practicum.ewm.enums.ParticipantRequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participant_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class ParticipantRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    LocalDateTime created;
    @Column(name = "user_id")
    Long userId;
    @Column(name = "event_id")
    Long eventId;
    @Enumerated(EnumType.STRING)
    ParticipantRequestStatus status;

}
