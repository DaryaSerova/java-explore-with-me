package ru.practicum.explore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.EndpointHitDto;
import ru.practicum.explore.dto.ViewStatsDto;
import ru.practicum.explore.service.StatService;

import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController("/")
public class StatsController {

    private final StatService statService;

    @PostMapping(value = "hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto createHit(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("Запрос к " + endpointHitDto.getApp() + " сохранен.");
        return statService.addHit(endpointHitDto);
    }

    @GetMapping(value = "stats")
    public List<ViewStatsDto> getViewStats(@RequestParam(required = false) String start,
                                           @RequestParam(required = false) String end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(required = false) boolean unique) {
        log.info("Получение статистики по посещениям.");
        return statService.getStatistics(start, end, uris, unique);

    }
}
