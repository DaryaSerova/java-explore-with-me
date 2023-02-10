package ru.practicum.explore.event.service;

import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.explore.event.StateEvent;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.dto.NewEventDto;
import ru.practicum.explore.event.dto.UpdateEventAdminRequestDto;
import ru.practicum.explore.event.model.UpdateEventUserRequestDto;

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

    void increment(Long eventId);


}
