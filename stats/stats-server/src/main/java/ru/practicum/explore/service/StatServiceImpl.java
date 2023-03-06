package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.EndpointHitDto;
import ru.practicum.explore.dto.ViewStatsDto;
import ru.practicum.explore.exceptions.EndpointEmptyAppException;
import ru.practicum.explore.jpa.StatPersistService;
import ru.practicum.explore.mapper.EndpointHitMapper;
import ru.practicum.explore.mapper.ViewStatsMapper;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final StatPersistService statPersistService;
    private final EndpointHitMapper endpointHitMapper;
    private final ViewStatsMapper viewStatsMapper;

    @Override
    public EndpointHitDto addHit(EndpointHitDto endpointHitdto) {

        if (endpointHitdto.getApp() == null || endpointHitdto.getApp().isEmpty()) {
            throw new EndpointEmptyAppException("App is empty.");
        }

        if (endpointHitdto.getUri() == null || endpointHitdto.getUri().isEmpty()) {
            throw new EndpointEmptyAppException("Uri is empty.");
        }

        return endpointHitMapper.toEndpointHitDto(statPersistService.addHit(
               endpointHitMapper.toEndpointHit(endpointHitdto)));
    }

    @Override
    public List<ViewStatsDto> getStatistics(String start, String end, List<String> uris, boolean unique) {

        LocalDateTime st = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8),
                DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));
        LocalDateTime en = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8),
                DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));

        var views = statPersistService.getViewStats(st, en, uris, unique);

        return views
                .stream()
                .map(view -> viewStatsMapper.toViewStatsDto(view))
                .collect(Collectors.toList());
    }

}
