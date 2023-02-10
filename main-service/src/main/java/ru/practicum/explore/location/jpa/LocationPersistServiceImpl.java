package ru.practicum.explore.location.jpa;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.location.model.Location;
import ru.practicum.explore.location.repository.LocationRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LocationPersistServiceImpl implements LocationPersistService {

    private final LocationRepository repository;

    public Optional<Location> findLocationById(Long id) {
        return repository.findById(id);
    }
}
