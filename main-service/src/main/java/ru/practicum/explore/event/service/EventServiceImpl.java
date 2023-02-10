package ru.practicum.explore.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.category.service.CategoryService;
import ru.practicum.explore.event.StateEvent;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.dto.NewEventDto;
import ru.practicum.explore.event.dto.UpdateEventAdminRequestDto;
import ru.practicum.explore.event.jpa.EventPersistService;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.event.model.UpdateEventUserRequestDto;
import ru.practicum.explore.exceptions.ForbiddenException;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.location.service.LocationService;
import ru.practicum.explore.user.service.UserService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final EventPersistService eventPersistService;
    private final EventMapper eventMapper;
    private final CategoryService categoryService;
    private final UserService userService;
    private final LocationService locationService;

    @Override
    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {

        var events = eventPersistService.findUserEvents(userId, from, size).getContent();

        if (events == null || events.isEmpty()) {

            return Collections.emptyList();
        }

        return events.stream()
                .map(event -> {
                    var category = categoryService.getCategoryById(event.getCategoryId());
                    var user = userService.getUserShortById(event.getInitiatorId());
                    return eventMapper.toEventShortDto(event, category, user);
                }).collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto eventDto) {

        var event = eventMapper.toEventWithUserId(userId, eventDto);

        if (!event.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
            throw new ForbiddenException("For the requested operation the conditions are not met.",
                    "Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " +
                            event.getEventDate());
        }

        event.setState(StateEvent.PENDING);
        event = eventPersistService.createEvent(event);

        var category = categoryService.getCategoryById(event.getCategoryId());
        var user = userService.getUserShortById(event.getInitiatorId());
        var location = locationService.getLocationById(event.getLocationId());

        return eventMapper.toFullEventDto(event, category, user, location);

    }

    @Override
    public EventFullDto getUserEventById(Long userId, Long eventId) {

        var event = eventPersistService.findUserEventById(userId, eventId);

        if (event == null) {
            throw new NotFoundException("The required object was not found.", "Event with %id was not found" + eventId);
        }

        var category = categoryService.getCategoryById(event.getCategoryId());
        var user = userService.getUserShortById(event.getInitiatorId());
        var location = locationService.getLocationById(event.getLocationId());

        return eventMapper.toFullEventDto(event, category, user, location);
    }

    @Override
    public EventFullDto updateEventById(Long userId, Long eventId, UpdateEventUserRequestDto updateEventDto) {

        var event = eventPersistService.findUserEventById(userId, eventId);
        var stateEvent = event.getState();
        var eventDate = updateEventDto.getEventDate();

        if (stateEvent.equals(StateEvent.PUBLISHED)) {
            throw new ForbiddenException("For the requested operation the conditions are not met.",
                    "Only pending or canceled events can be changed");
        }

        if (event == null) {
            throw new NotFoundException("The required object was not found.",
                    "Event with %id was not found " + eventId);
        }

        if (!eventDate.isAfter(LocalDateTime.now().plusHours(2))) {
            throw new ForbiddenException("For the requested operation the conditions are not met.",
                    "Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " + eventDate);
        }

        eventMapper.mergeToEvent(updateEventDto, event);
        event.setInitiatorId(userId);

        var category = categoryService.getCategoryById(event.getCategoryId());
        var user = userService.getUserShortById(event.getInitiatorId());
        var location = locationService.getLocationById(event.getLocationId());

        var resultEvent = eventMapper.toFullEventDto(
                eventPersistService.updateEvent(event), category, user, location);
        return resultEvent;
    }

    @Override
    public List<EventFullDto> getFullEvents(List<Long> users, List<StateEvent> states, List<Long> categories,
                                            String rangeStart, String rangeEnd, int from, int size) {

        var events = eventPersistService.getFullEvents(
                users, states, categories, rangeStart, rangeEnd, from, size).getContent();

        if (events.isEmpty()) {
            return Collections.emptyList();
        }

        return events.stream()
                .map(event -> {
                    var category = categoryService.getCategoryById(event.getCategoryId());
                    var user = userService.getUserShortById(event.getInitiatorId());
                    var location = locationService.getLocationById(event.getLocationId());
                    return eventMapper.toFullEventDto(event, category, user, location);
                }).collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventByAdmin(UpdateEventAdminRequestDto updateEventDto, Long eventId) {

        var event = eventPersistService.findEventById(eventId).get();
        var eventDate = updateEventDto.getEventDate();
        var createdOn = event.getCreatedOn();
        var stateEvent = event.getState();

        LocalDateTime startUpdateEvent = LocalDateTime.parse(
                URLDecoder.decode(String.valueOf(eventDate), StandardCharsets.UTF_8),
                DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));


        if (!startUpdateEvent.isAfter(createdOn.plusHours(1))) {
            throw new ForbiddenException("For the requested operation the conditions are not met.",
                    "Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " + createdOn);
        }


        return null;
    }

    @Override
    public void increment(Long eventId) {

        var event = eventPersistService.findEventById(eventId).get();

        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventPersistService.saveEvent(event);
    }

    @Override
    public EventFullDto findEventById(Long eventId) {

        var event = eventPersistService.findEventById(eventId).get();

        var category = categoryService.getCategoryById(event.getCategoryId());
        var user = userService.getUserShortById(event.getInitiatorId());
        var location = locationService.getLocationById(event.getLocationId());

        return eventMapper.toFullEventDto(event, category, user, location);

    }

}
