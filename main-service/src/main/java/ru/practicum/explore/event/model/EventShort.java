package ru.practicum.explore.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class EventShort {

    private String annotation;

    private Long categoryId;

    private Integer confirmedRequests;

    private LocalDateTime eventDate;

    private Long id;

    private Long initiatorId;

    private boolean paid;

    private String title;

    private Integer views;
}
