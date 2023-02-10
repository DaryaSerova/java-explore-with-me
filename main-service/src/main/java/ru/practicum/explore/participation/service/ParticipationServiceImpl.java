package ru.practicum.explore.participation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.event.jpa.EventPersistService;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.service.EventService;
import ru.practicum.explore.exceptions.ConflictException;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.participation.ParticipationStatus;
import ru.practicum.explore.participation.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.explore.participation.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.explore.participation.dto.ParticipationRequestDto;
import ru.practicum.explore.participation.jpa.ParticipationPersistService;
import ru.practicum.explore.participation.mapper.ParticipationMapper;
import ru.practicum.explore.participation.model.ParticipationRequest;
import ru.practicum.explore.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.explore.participation.ParticipationStatus.CONFIRMED;

@RequiredArgsConstructor
@Service
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationPersistService participationPersistService;
    private final ParticipationMapper participationMapper;
    private final EventService eventService;
    private final EventPersistService eventPersistService;
    private final UserService userService;

    @Override
    public List<ParticipationRequestDto> getEventParticipantsByEventId(Long userId, Long eventId) {

        var event = eventService.getUserEventById(userId, eventId);

        return participationPersistService.getEventParticipantsByEventId(eventId)
                .stream()
                .map(participationMapper::map)
                .collect(Collectors.toList());

    }

    @Override
    public EventRequestStatusUpdateResultDto changeRequestStatus(
            Long userId, Long eventId, EventRequestStatusUpdateRequestDto requestStatusUpdateRequestDto) {

        var event = eventService.getUserEventById(userId, eventId);

        if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                    "The participant limit has been reached");
        }
        var result = new EventRequestStatusUpdateResultDto();
        requestStatusUpdateRequestDto.getRequestIds().forEach(requestId -> {
            var requestOpt = participationPersistService.getEventParticipantsById(requestId);
            if (requestOpt.isPresent()) {
                var request = requestOpt.get();
                if (request.getStatus() == CONFIRMED) {
                    throw new ConflictException("The participant  already has status : CONFIRMED",
                            "For the requested operation the conditions are not met.");
                }
                request.setStatus(getStatus(userId, eventId));

                request = participationPersistService.save(request);

                if (request.getStatus().equals(CONFIRMED)) {
                    eventService.increment(eventId);
                }

                result.getConfirmedRequests().add(participationMapper.map(request));
            }
        });
        return result;
    }

    @Override
    public List<ParticipationRequestDto> getUserParticipantsRequests(Long userId) {

        Optional<ParticipationRequest> requests = participationPersistService.getEventParticipantsByUserId(userId);

        if (requests.isEmpty()) {
            return Collections.emptyList();
        }

        userService.getUserShortById(userId);

        return participationPersistService.getEventParticipantsByUserId(userId)
                .stream()
                .map(participationMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId) {

        Optional<Event> event = eventPersistService.findEventById(eventId);

        if (event.isEmpty()) {
            throw new NotFoundException("The required object was not found.", "Event with %id was not found" + eventId);
        }

        getEventParticipantsByEventId(userId, eventId);

        ParticipationRequest participation = new ParticipationRequest();
        participation.setRequesterId(userId);
        participation.setEventId(eventId);
        participation.setStatus(ParticipationStatus.PENDING);

        return participationMapper.map(participationPersistService.addParticipationRequest(participation));

    }

    private ParticipationStatus getStatus(Long userId, Long eventId) {

        var event = eventService.getUserEventById(userId, eventId);

        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            return CONFIRMED;
        }
        if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            return ParticipationStatus.REJECTED;
        }
        return CONFIRMED;
    }


}
