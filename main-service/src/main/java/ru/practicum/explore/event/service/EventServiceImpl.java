package ru.practicum.explore.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.admin.StateAction;
import ru.practicum.explore.category.service.CategoryService;
import ru.practicum.explore.event.StateEvent;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.dto.NewEventDto;
import ru.practicum.explore.event.dto.UpdateEventAdminRequestDto;
import ru.practicum.explore.event.jpa.EventPersistService;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.dto.UpdateEventUserRequestDto;
import ru.practicum.explore.exceptions.BadRequestException;
import ru.practicum.explore.exceptions.ConflictException;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.location.service.LocationService;
import ru.practicum.explore.statistic.StatisticService;
import ru.practicum.explore.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
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
    private final StatisticService statisticService;


    @Override
    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {

        var events = eventPersistService.findUserEvents(userId, from, size).getContent();

        if (events.isEmpty()) {

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
        if (eventDto.getAnnotation() == null) {
            throw new BadRequestException("Bad request body", "Event annotation is empty");
        }
        var location = locationService.save(eventDto.getLocation());
        eventDto.setLocation(location);

        var event = eventMapper.toEventWithUserId(userId, eventDto);

        if (!event.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                    "Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                            "Value: " + event.getEventDate());
        }

        event.setState(StateEvent.PENDING);
        event = eventPersistService.createEvent(event);

        var category = categoryService.getCategoryById(event.getCategoryId());
        var user = userService.getUserShortById(event.getInitiatorId());

        return eventMapper.toFullEventDto(event, category, user, location);

    }

    @Override
    public EventFullDto getUserEventById(Long userId, Long eventId) {

        var event = eventPersistService.findUserEventById(userId, eventId);

        if (event == null) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Event with id = %s was not found", eventId));
        }

        var category = categoryService.getCategoryById(event.getCategoryId());
        var user = userService.getUserShortById(event.getInitiatorId());
        var location = locationService.getLocationById(event.getLocation().getId());

        return eventMapper.toFullEventDto(event, category, user, location);
    }

    @Override
    public EventFullDto updateEventById(Long userId, Long eventId, UpdateEventUserRequestDto updateEventDto) {

        var event = eventPersistService.findUserEventById(userId, eventId);
        var stateEvent = event.getState();
        var updateEventDate = updateEventDto.getEventDate();

        if (StateEvent.PUBLISHED.equals(stateEvent)) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                    "Only pending or canceled events can be changed");
        }

        if (event.getId() == null) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Event with id = %s was not found ", eventId));
        }

        if (updateEventDate != null && !updateEventDate.isAfter(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                    "Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                            "Value: " + updateEventDate);
        }

        eventMapper.mergeToEvent(updateEventDto, event);
        event.setState(getStateEvent(event, updateEventDto.getStateAction()));
        event.setInitiatorId(userId);

        var category = categoryService.getCategoryById(event.getCategoryId());
        var user = userService.getUserShortById(event.getInitiatorId());
        var location = locationService.getLocationById(event.getLocation().getId());

        return eventMapper.toFullEventDto(eventPersistService.updateEvent(event), category, user, location);
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
                    var location = locationService.getLocationById(event.getLocation().getId());
                    return eventMapper.toFullEventDto(event, category, user, location);
                }).collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventByAdmin(UpdateEventAdminRequestDto updateEventDto, Long eventId) {

        var eventOpt = eventPersistService.findEventById(eventId);

        if (eventOpt.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Event with id = %s was not found", eventId));
        }

        var event = eventOpt.get();

        var eventUpdateDate = updateEventDto.getEventDate();
        var createdOn = event.getCreatedOn();

        if (eventUpdateDate != null && !eventUpdateDate.isAfter(createdOn.plusHours(1))) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                    "Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                            "Value: " + createdOn);
        }

        if (StateAction.PUBLISH_EVENT.equals(updateEventDto.getStateAction())) {

            if (StateEvent.PUBLISHED.equals(event.getState()) || StateEvent.CANCELED.equals(event.getState())) {
                throw new ConflictException("For the requested operation the conditions are not met.",
                        "Cannot publish the event because it's not in the right state: PUBLISHED");
            }
            eventMapper.mergeToEventAdmin(updateEventDto, event);
            event.setState(StateEvent.PUBLISHED);

            var category = categoryService.getCategoryById(event.getCategoryId());
            var user = userService.getUserShortById(event.getInitiatorId());
            var location = locationService.getLocationById(event.getLocation().getId());

            return eventMapper.toFullEventDto(eventPersistService.updateEvent(event), category, user, location);
        }

        if (StateAction.REJECT_EVENT.equals(updateEventDto.getStateAction())) {
            if (event.getState().equals(StateEvent.PUBLISHED)) {
                throw new ConflictException("For the requested operation the conditions are not met.",
                        "Cannot publish the event because it's not in the right state: PUBLISHED");
            }
            eventMapper.mergeToEventAdmin(updateEventDto, event);
            event.setState(StateEvent.CANCELED);

            var category = categoryService.getCategoryById(event.getCategoryId());
            var user = userService.getUserShortById(event.getInitiatorId());
            var location = locationService.getLocationById(event.getLocation().getId());

            return eventMapper.toFullEventDto(eventPersistService.updateEvent(event), category, user, location);
        }

        throw new ConflictException("For the requested operation the conditions are not met.",
                "Cannot publish the event because it's not in the right state: PUBLISHED");
    }

    @Override
    public List<EventShortDto> getEventsPublic(String text, List<Long> categories, Boolean paid,
                                               String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                               String sort, int from, int size, HttpServletRequest request) {

        var eventsPublic = eventPersistService.getEventsPublic(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size).getContent();

        if (eventsPublic.isEmpty()) {
            return Collections.emptyList();
        }

        var end = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());

        return eventsPublic.stream()
                .map(event -> {
                    var category = categoryService.getCategoryById(event.getCategoryId());
                    var user = userService.getUserShortById(event.getInitiatorId());

                    var stat =
                            statisticService.getViews("1900-01-01 00:00:00", end,
                            List.of(request.getRequestURI() + "/" + event.getId()), true);
                    var view = stat.isEmpty() ? 0 : stat.get(0).getHits();
                    event.setViews(view);


                    return eventMapper.toEventShortDto(event, category, user);
                }).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventPublicById(Long id, HttpServletRequest request) {

        var event = findEventById(id);

        if (event == null) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Event with id = %s was not found", id));
        }
        if (!StateEvent.PUBLISHED.equals(event.getState())) {
            return null;
        }

        var end = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());

        var stat = statisticService.getViews("1900-01-01 00:00:00", end,
                                    List.of(request.getRequestURI() + "/" + event.getId()), true);

        var view = stat.isEmpty() ? 0 : stat.get(0).getHits();
        event.setViews(view);

        return event;
    }

    @Override
    public EventFullDto findEventById(Long eventId) {

        var eventOpt = eventPersistService.findEventById(eventId);

        if (eventOpt.isPresent()) {

            var event = eventOpt.get();

            var category = categoryService.getCategoryById(event.getCategoryId());
            var user = userService.getUserShortById(event.getInitiatorId());
            var location = locationService.getLocationById(event.getLocation().getId());

            return eventMapper.toFullEventDto(event, category, user, location);
        }

        throw new NotFoundException("The required object was not found.",
                String.format("Event with id = %s was not found", eventId));
    }

    @Override
    public EventShortDto findEventShortById(Long eventId) {

        var eventOpt = eventPersistService.findEventById(eventId);

        if (eventOpt.isPresent()) {

            var event = eventOpt.get();
            var category = categoryService.getCategoryById(event.getCategoryId());
            var user = userService.getUserShortById(event.getInitiatorId());

            return eventMapper.toEventShortDto(event, category, user);
        }

        throw new NotFoundException("The required object was not found.",
                String.format("Event with id = %s was not found ", eventId));

    }

    @Override
    public void increment(Long eventId) {

        var eventOpt = eventPersistService.findEventById(eventId);

        if (eventOpt.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Event with id = %s was not found ", eventId));
        }

        var event = eventOpt.get();

        var confirmedRequests =
                event.getConfirmedRequests() == null ||
                        event.getConfirmedRequests() == 0 ? 1
                        : event.getConfirmedRequests() + 1;
        event.setConfirmedRequests(confirmedRequests);
        eventPersistService.saveEvent(event);


    }

    @Override
    public void decrement(Long eventId) {

        var eventOpt = eventPersistService.findEventById(eventId);

        if (eventOpt.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Event with id = %s was not found ", eventId));
        }

        var event = eventOpt.get();

        var confirmedRequests =
                event.getConfirmedRequests() == null ||
                        event.getConfirmedRequests() == 0 ? 0
                        : event.getConfirmedRequests() - 1;
        event.setConfirmedRequests(confirmedRequests);
        eventPersistService.saveEvent(event);

    }


    private StateEvent getStateEvent(Event event, StateAction stateAction) {

        switch (stateAction) {
            case CANCEL_REVIEW:
                return StateEvent.CANCELED;
            case PUBLISH_EVENT:
                return StateEvent.PUBLISHED;
            case SEND_TO_REVIEW:
                return StateEvent.PENDING;
            default:
                return event.getState();

        }

    }

}
