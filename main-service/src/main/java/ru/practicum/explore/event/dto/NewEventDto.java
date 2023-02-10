package ru.practicum.explore.event.dto;

import lombok.*;
import ru.practicum.explore.location.model.Location;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NewEventDto {

    @NotNull
    private String annotation;
    @NotNull
    private Long category;
    @NotNull
    private String description;
    @NotNull
    private LocalDateTime eventDate;
    @NotNull
    private Location location;

    private boolean paid;

    private Integer participantLimit;

    private boolean requestModeration;
    @NotNull
    private String title;
}
