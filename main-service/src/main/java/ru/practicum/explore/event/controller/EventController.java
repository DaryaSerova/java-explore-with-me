package ru.practicum.explore.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.client.StatsClient;
import ru.practicum.explore.dto.EndpointHitDto;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventController {

    private final EventService eventService;

    private final StatsClient client;

    @GetMapping
    public List<EventShortDto> getEventsPublic(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) List<Long> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false) String rangeStart,
                                               @RequestParam(required = false) String rangeEnd,
                                               @RequestParam(required = false, value = "onlyAvailable",
                                                             defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(required = false) String sort,
                                               @RequestParam(required = false, value = "from", defaultValue = "0")
                                               @PositiveOrZero int from,
                                               @RequestParam(required = false, value = "size", defaultValue = "10")
                                               @PositiveOrZero int size,
                                               HttpServletRequest request) {

        EndpointHitDto hitDto = new EndpointHitDto();
        hitDto.setApp("main-service");
        hitDto.setIp(request.getRemoteAddr());
        hitDto.setUri(request.getRequestURI());
        hitDto.setTimestamp(LocalDateTime.now());
        client.addHit(hitDto);

        log.info("Получение опубликованных событий.");
        return eventService.getEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size);

    }

    @GetMapping("/{id}")
    public EventFullDto getEventPublicById(@PathVariable(name = "id") Long id, HttpServletRequest request) {

        EndpointHitDto hitDto = new EndpointHitDto();
        hitDto.setApp("main-service");
        hitDto.setIp(request.getRemoteAddr());
        hitDto.setUri(request.getRequestURI());
        hitDto.setTimestamp(LocalDateTime.now());
        client.addHit(hitDto);

        log.info("Получение информации о событий с id = " + id);
        return eventService.getEventPublicById(id);

    }
}
