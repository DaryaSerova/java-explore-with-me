package ru.practicum.explore.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore.dto.ViewStatsDto;
import ru.practicum.explore.model.ViewStats;

@Mapper(componentModel = "spring")
public interface ViewStatsMapper {

    ViewStats toViewStats(ViewStatsDto viewStatsDto);

    ViewStatsDto toViewStatsDto(ViewStats viewStats);
}
