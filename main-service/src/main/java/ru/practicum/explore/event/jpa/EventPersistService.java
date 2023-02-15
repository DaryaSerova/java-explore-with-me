package ru.practicum.explore.event.jpa;

import org.springframework.data.domain.Page;
import ru.practicum.explore.event.StateEvent;
import ru.practicum.explore.event.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventPersistService {

    Event createEvent(Event event);

    Page<Event> findUserEvents(Long userId, Integer from, Integer size);

    Event findUserEventById(Long userId, Long eventId);

    Event updateEvent(Event event);

    Optional<Event> findEventById(Long eventId);

    void saveEvent(Event event);

    Page<Event> getFullEvents(List<Long> users, List<StateEvent> states, List<Long> categories,
                              String rangeStart, String rangeEnd, int from, int size);

    Page<Event> getEventsPublic(String text, List<Long> categories, Boolean paid,
                                String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                String sort, int from, int size);
}
