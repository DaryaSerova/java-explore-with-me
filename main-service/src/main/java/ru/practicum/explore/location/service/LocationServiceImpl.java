package ru.practicum.explore.location.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
            //throw
        }

        return locationMapper.map(locationOpt.get());
    }
}
