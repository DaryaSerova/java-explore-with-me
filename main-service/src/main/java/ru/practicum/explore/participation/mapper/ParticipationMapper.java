package ru.practicum.explore.participation.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explore.participation.dto.ParticipationRequestDto;
import ru.practicum.explore.participation.model.ParticipationRequest;

@Mapper(componentModel = "spring")
public interface ParticipationMapper {

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "event", source = "eventId")
    @Mapping(target = "requester", source = "requesterId")
    ParticipationRequestDto toParticipationDto(ParticipationRequest entity);
}
