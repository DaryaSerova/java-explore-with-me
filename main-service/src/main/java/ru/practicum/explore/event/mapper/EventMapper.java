package ru.practicum.explore.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.event.dto.EventDto;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.dto.NewEventDto;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.UpdateEventUserRequestDto;
import ru.practicum.explore.location.dto.LocationDto;
import ru.practicum.explore.user.dto.UserShortDto;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {

    Event toEvent(EventDto eventDto);

    EventDto toEventDto(Event event);

    @Mapping(target = "id", source = "event.id")
    EventFullDto toFullEventDto(Event event, CategoryDto category,
                                UserShortDto initiator, LocationDto location);

    @Mapping(target = "id", source = "event.id")
    EventShortDto toEventShortDto(Event event, CategoryDto category,
                                  UserShortDto initiator);

    @Mapping(target = "initiatorId", source = "userId")
    Event toEventWithUserId(Long userId, NewEventDto eventDto);

    void mergeToEvent(UpdateEventUserRequestDto updateEventDto, @MappingTarget Event event);


}
//    @Mapping(target = "bookerId", source = "entity.booker.id")
//    @Mapping(target = "itemId", source = "entity.item.id")
//    BookingCreateDto toDto(Booking entity);