package ru.practicum.explore.participation.jpa;

import ru.practicum.explore.participation.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

public interface ParticipationPersistService {


    List<ParticipationRequest> getEventParticipantsByEventId(Long eventId);

    Optional<ParticipationRequest> getEventParticipantsById(Long id);

    ParticipationRequest save(ParticipationRequest request);

    Optional<ParticipationRequest> getEventParticipantsByUserId(Long userId);

    ParticipationRequest addParticipationRequest(ParticipationRequest participation);

    ParticipationRequest findParticipationByRequesterIdAndEventId(Long userId, Long eventId);
}
