package ru.practicum.explore.jpa;

import ru.practicum.explore.model.EndpointHit;
import ru.practicum.explore.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatPersistService {

    EndpointHit addHit(EndpointHit endpointHit);

    List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

}
