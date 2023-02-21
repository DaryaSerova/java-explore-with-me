package ru.practicum.explore.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.event.dto.*;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.dto.UpdateEventUserRequestDto;
import ru.practicum.explore.location.dto.LocationDto;
import ru.practicum.explore.location.mapper.LocationMapper;
import ru.practicum.explore.user.dto.UserShortDto;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {LocationMapper.class})
public interface EventMapper {

    @Mapping(target = "id", source = "event.id")
    EventFullDto toFullEventDto(Event event, CategoryDto category,
                                UserShortDto initiator, LocationDto location);

    @Mapping(target = "id", source = "event.id")
    EventShortDto toEventShortDto(Event event, CategoryDto category,
                                  UserShortDto initiator);

    @Mapping(target = "initiatorId", source = "userId")
    @Mapping(target = "categoryId", source = "eventDto.category")
    @Mapping(target = "location", source = "eventDto.location")
    Event toEventWithUserId(Long userId, NewEventDto eventDto);

    void mergeToEvent(UpdateEventUserRequestDto updateEventDto, @MappingTarget Event event);

    void mergeToEventAdmin(UpdateEventAdminRequestDto updateEventAdminDto, @MappingTarget Event event);

}