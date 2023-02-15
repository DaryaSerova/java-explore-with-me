package ru.practicum.explore.location.service;

import ru.practicum.explore.location.dto.LocationDto;

public interface LocationService {

    LocationDto getLocationById(Long id);

    LocationDto save(LocationDto locationDto);
}
