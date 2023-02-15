package ru.practicum.explore.participation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.event.StateEvent;
import ru.practicum.explore.event.service.EventService;
import ru.practicum.explore.exceptions.BadRequestException;
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

import static ru.practicum.explore.participation.ParticipationStatus.*;

@RequiredArgsConstructor
@Service
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationPersistService participationPersistService;
    private final ParticipationMapper participationMapper;
    private final EventService eventService;
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

        if (event.getConfirmedRequests() != null && event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                                        "The participant limit has been reached");
        }

        var result = new EventRequestStatusUpdateResultDto();

        requestStatusUpdateRequestDto.getRequestIds().forEach(requestId -> {
            var requestOpt = participationPersistService.getEventParticipantsById(requestId);

            if (requestOpt.isPresent()) {
                var request = requestOpt.get();

                if (request.getStatus() != PENDING) {
                    throw new BadRequestException("Incorrectly made request.", "Request must have status PENDING");
                }

                if (requestStatusUpdateRequestDto.getStatus().equals(REJECTED)) {
                    request.setStatus(REJECTED);
                    result.getRejectedRequests().add(participationMapper.map(request));

                } else {
                    request.setStatus(getStatus(userId, eventId));
                }

                request = participationPersistService.save(request);

                if (request.getStatus().equals(CONFIRMED)) {
                    eventService.increment(eventId);
                    result.getConfirmedRequests().add(participationMapper.map(request));
                }
            }
        });
        return result;
    }

    @Override
    public List<ParticipationRequestDto> getUserParticipantsRequests(Long userId) {

        userService.getUserShortById(userId);

        Optional<ParticipationRequest> requests = participationPersistService.getEventParticipantsByUserId(userId);

        if (requests.isEmpty()) {
            return Collections.emptyList();
        }

        return participationPersistService.getEventParticipantsByUserId(userId)
                .stream()
                .map(participationMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId) {

        var participation =
                participationPersistService.findParticipationByRequesterIdAndEventId(userId, eventId);

        if (participation != null &&
                participation.getRequesterId().equals(userId)
                && participation.getEventId().equals(eventId)) {
            throw new ConflictException("Integrity constraint has been violated.",
                                        "could not execute statement; SQL [n/a]; constraint [uq_request]; " +
                                        "nested exception is org.hibernate.exception.ConstraintViolationException: " +
                                        "could not execute statement");
        }

        var event = eventService.findEventById(eventId);


        if (event.getInitiator().getId().equals(userId) || !event.getState().equals(StateEvent.PUBLISHED)) {
            throw new ConflictException("Integrity constraint has been violated.",
                                        "could not execute statement; SQL [n/a]; constraint [uq_request]; " +
                                        "nested exception is org.hibernate.exception.ConstraintViolationException: " +
                                        "could not execute statement");
        }

        if (event.getConfirmedRequests() != null &&
                event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                                        "The participant limit has been reached");
        }

        ParticipationRequest newParticipation = new ParticipationRequest();

        newParticipation.setRequesterId(userId);
        newParticipation.setEventId(eventId);

        if (event.getRequestModeration() != null && !event.getRequestModeration()) {
            newParticipation.setStatus(ParticipationStatus.CONFIRMED);
            eventService.increment(event.getId());
        } else {
            newParticipation.setStatus(ParticipationStatus.PENDING);
        }

        return participationMapper.map(participationPersistService.addParticipationRequest(newParticipation));

    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {

        var request = participationPersistService.getEventParticipantsById(requestId).get();

        if (request == null) {
            throw new NotFoundException("The required object was not found.",
                          String.format("Request with id = %requestId was not found", requestId));
        }

        request.setStatus(ParticipationStatus.CANCELED);

        request = participationPersistService.save(request);

        if (request.getStatus().equals(REJECTED)) {
            eventService.decrement(request.getEventId());
        }

        return participationMapper.map(request);

    }


    private ParticipationStatus getStatus(Long userId, Long eventId) {

        var event = eventService.getUserEventById(userId, eventId);

        if (event.getParticipantLimit() == 0 ||
                (event.getRequestModeration() != null && !event.getRequestModeration())) {
            return CONFIRMED;
        }

        if (event.getConfirmedRequests() != null &&
                event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            return ParticipationStatus.REJECTED;
        }
        return CONFIRMED;
    }

}
