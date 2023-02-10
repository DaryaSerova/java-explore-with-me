package ru.practicum.explore.location.jpa;

import ru.practicum.explore.location.model.Location;

import java.util.Optional;

public interface LocationPersistService {
    Optional<Location> findLocationById(Long id);
}
