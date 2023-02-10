package ru.practicum.explore.location.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore.location.dto.LocationDto;
import ru.practicum.explore.location.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {


    LocationDto map(Location entity);
}
