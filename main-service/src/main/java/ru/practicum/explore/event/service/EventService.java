package ru.practicum.explore.event.service;

import ru.practicum.explore.event.StateEvent;
import ru.practicum.explore.event.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size);

    EventFullDto createEvent(Long userId, NewEventDto eventDto);

    EventFullDto getUserEventById(Long userId, Long eventId);

    EventFullDto updateEventById(Long userId, Long eventId, UpdateEventUserRequestDto updateEvent);

    List<EventFullDto> getFullEvents(List<Long> users, List<StateEvent> states, List<Long> categories,
                                     String rangeStart, String rangeEnd, int from, int size);

    EventFullDto updateEventByAdmin(UpdateEventAdminRequestDto updateEventDto, Long eventId);

    EventFullDto findEventById(Long eventId);

    List<EventShortDto> getEventsPublic(String text, List<Long> categories, Boolean paid,
                                        String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                        String sort, int from, int size, HttpServletRequest request);

    EventFullDto getEventPublicById(Long id, HttpServletRequest request);

    EventShortDto findEventShortById(Long eventId);

    void increment(Long eventId);

    void decrement(Long eventId);

}
