package ru.practicum.ewm.models.request.participantRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.enums.ParticipantRequestStatus;

import javax.persistence.*;

@Entity
@Table(name = "participant_request")
@Getter
@Setter
@NoArgsConstructor
public class ParticipantRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "user_id")
    Long userId;
    @Column(name = "event_id")
    Long eventId;
    @Enumerated(EnumType.STRING)
    ParticipantRequestStatus status;

}
