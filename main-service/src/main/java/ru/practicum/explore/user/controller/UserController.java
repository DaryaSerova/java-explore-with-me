package ru.practicum.explore.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.dto.NewEventDto;
import ru.practicum.explore.event.model.UpdateEventUserRequestDto;
import ru.practicum.explore.event.service.EventService;
import ru.practicum.explore.participation.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.explore.participation.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.explore.participation.dto.ParticipationRequestDto;
import ru.practicum.explore.participation.service.ParticipationService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final EventService eventService;
    private final ParticipationService participationService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getUserEvents(@PathVariable("userId") Long userId,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(defaultValue = "10") @PositiveOrZero Integer size) {
        log.info("Получение событий текущего пользователя с id = " + userId);
        return eventService.getUserEvents(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable("userId") Long userId,
                                    @RequestBody NewEventDto eventDto) {
        log.info("Добавление события.");
        return eventService.createEvent(userId, eventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getUserEventById(@PathVariable("userId") Long userId,
                                         @PathVariable("eventId") Long eventId) {
        log.info("Получение полной информации о событии с id = " + eventId +
                " текущего пользователя с id = " + userId);
        return eventService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEventById(@PathVariable("userId") Long userId,
                                        @PathVariable("eventId") Long eventId,
                                        @RequestBody UpdateEventUserRequestDto updateEvent) {
        log.info("Обновление информации о событии с id = " + eventId + " текущего пользователя с id = " + userId);

        return eventService.updateEventById(userId, eventId, updateEvent);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventParticipantsByEventId(@PathVariable("userId") Long userId,
                                                                       @PathVariable("eventId") Long eventId) {
        log.info("Получение информации о запросах на участие в событии c id " + eventId +
                "текущего пользователя с id " + userId);
        return participationService.getEventParticipantsByEventId(userId, eventId);
    }


    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResultDto changeRequestStatus(@PathVariable("userId") Long userId,
                                                                 @PathVariable("eventId") Long eventId,
                                                                 @RequestBody EventRequestStatusUpdateRequestDto
                                                                         requestStatusUpdateRequestDto) {
        log.info("Изменение статуса заявок на участие в событии c id " + eventId +
                "текущего пользователя с id " + userId);
        return participationService.changeRequestStatus(userId, eventId, requestStatusUpdateRequestDto);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable("userId") Long userId) {
        log.info("Запрос на получение информации о заявках пользователя с id " + userId +
                " на участие в чужих событиях");
        return participationService.getUserParticipantsRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addParticipationRequest(@PathVariable("userId") Long userId,
                                                           @RequestParam Long eventId) {
        log.info("Запрос на добавление запроса от текущего пользователя с id " + userId + " на участие в событии");
        return participationService.addParticipationRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable("userId") Long userId,
                                                 @PathVariable("requestId") Long requestId) {
        log.info("Отмена запроса на участие в событии c id " + requestId + " пользователя с id " + userId);
        return participationService.cancelRequest(userId, requestId);
    }

}
