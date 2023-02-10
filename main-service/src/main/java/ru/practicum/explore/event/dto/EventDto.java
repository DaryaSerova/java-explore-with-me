package ru.practicum.explore.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.event.StateEvent;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class EventDto {

    private String annotation;

    private Long categoryId;

    private Integer confirmedRequests;

    private LocalDateTime createdOn;

    private String description;

    private LocalDateTime eventDate;

    private Long id;

    private Long initiatorId;

    private Long locationId;

    private boolean paid;

    private Integer participantLimit;

    private LocalDateTime publishedOn;

    private boolean requestModeration;

    private StateEvent state;

    private String title;

    private Integer views;
}
