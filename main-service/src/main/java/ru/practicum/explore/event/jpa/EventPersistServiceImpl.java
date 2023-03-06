package ru.practicum.explore.event.jpa;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.event.SortState;
import ru.practicum.explore.event.StateEvent;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.event.repository.specification.EventSpecification;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventPersistServiceImpl implements EventPersistService {

    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Page<Event> findUserEvents(Long userId, Integer from, Integer size) {
        return eventRepository.findAllByInitiatorId(userId, PageRequest.of(from, size));
    }

    @Override
    public Event findUserEventById(Long userId, Long eventId) {
        return eventRepository.findEventByIdAndInitiatorId(eventId, userId);
    }

    @Override
    @Transactional
    public Event updateEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Optional<Event> findEventById(Long eventId) {
        return eventRepository.findById(eventId);
    }

    @Override
    @Transactional
    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    @Override
    public Page<Event> getFullEvents(List<Long> users, List<StateEvent> states, List<Long> categories,
                                     String rangeStart, String rangeEnd, int from, int size) {

        return eventRepository.findAll(EventSpecification.requestSpec(users, states, categories, rangeStart, rangeEnd),
                PageRequest.of(from, size));
    }

    @Override
    public Page<Event> getEventsPublic(String text, List<Long> categories, Boolean paid,
                                       String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                       String sort, int from, int size) {

        var page = PageRequest.of(from, size);

        if (sort != null && !sort.isEmpty()) {
            var sortD = Sort.by(SortState.EVENT_DATE.getField().equals(sort) ?
                    SortState.EVENT_DATE.getField() : SortState.VIEWS.getField());
            page.withSort(sortD);
        }

        return eventRepository.findAll(EventSpecification.requestSpec(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, StateEvent.PUBLISHED), page);
    }
}
