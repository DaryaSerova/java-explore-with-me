package ru.practicum.explore.participation.service;

import ru.practicum.explore.participation.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.explore.participation.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.explore.participation.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationService {

    List<ParticipationRequestDto> getEventParticipantsByEventId(Long userId, Long eventId);

    EventRequestStatusUpdateResultDto changeRequestStatus(
            Long userId, Long eventId, EventRequestStatusUpdateRequestDto requestStatusUpdateRequestDto);

    List<ParticipationRequestDto> getUserParticipantsRequests(Long userId);

    ParticipationRequestDto addParticipationRequest(Long userId, Long eventId);
}
