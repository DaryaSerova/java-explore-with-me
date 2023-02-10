package ru.practicum.explore.participation.jpa;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.participation.model.ParticipationRequest;
import ru.practicum.explore.participation.repository.ParticipationRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ParticipationPersistServiceImpl implements ParticipationPersistService {

    private final ParticipationRepository participationRepository;

    @Override
    public List<ParticipationRequest> getEventParticipantsByEventId(Long eventId) {
        return participationRepository.findAllByEventId(eventId);
    }

    @Override
    public Optional<ParticipationRequest> getEventParticipantsById(Long id) {
        return participationRepository.findById(id);
    }

    @Override
    @Transactional
    public ParticipationRequest save(ParticipationRequest request) {
        return participationRepository.save(request);
    }

    @Override
    public Optional<ParticipationRequest> getEventParticipantsByUserId(Long userId) {
        return participationRepository.findParticipationRequestByRequesterId(userId);
    }

    @Override
    @Transactional
    public ParticipationRequest addParticipationRequest(ParticipationRequest participation) {
        return participationRepository.save(participation);
    }
}
