package ru.practicum.explore.location.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.location.dto.LocationDto;
import ru.practicum.explore.location.jpa.LocationPersistService;
import ru.practicum.explore.location.mapper.LocationMapper;

@RequiredArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

    private final LocationMapper locationMapper;

    private final LocationPersistService locationPersistService;

    public LocationDto getLocationById(Long id) {

        var locationOpt = locationPersistService.findLocationById(id);
        if (locationOpt.isEmpty()) {
            throw new NotFoundException("The required object was not found.",
                    String.format("Location with id = %s was not found", id));
        }

        return locationMapper.toLocationDto(locationOpt.get());
    }

    @Override
    public LocationDto save(LocationDto locationDto) {
        return locationMapper.toLocationDto(locationPersistService
                .save(locationMapper.toLocation(locationDto)));
    }
}
