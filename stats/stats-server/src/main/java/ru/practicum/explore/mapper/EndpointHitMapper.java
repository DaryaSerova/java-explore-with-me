package ru.practicum.explore.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore.dto.EndpointHitDto;
import ru.practicum.explore.model.EndpointHit;

@Mapper(componentModel = "spring")
public interface EndpointHitMapper {

    EndpointHit toEndpointHit(EndpointHitDto endpointHitDto);

    EndpointHitDto toEndpointHitDto(EndpointHit endpointHit);
}
