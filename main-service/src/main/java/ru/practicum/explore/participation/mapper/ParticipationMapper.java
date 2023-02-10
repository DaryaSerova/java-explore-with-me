package ru.practicum.explore.participation.mapper;


import org.mapstruct.Mapper;
import ru.practicum.explore.participation.dto.ParticipationRequestDto;
import ru.practicum.explore.participation.model.ParticipationRequest;

@Mapper(componentModel = "spring")
public interface ParticipationMapper {


    ParticipationRequestDto map(ParticipationRequest entity);
}
