package ru.practicum.explore.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class NewEvent {

    @Length(max = 2000, min = 20)
    private String annotation;

    @NotBlank
    private Long categoryId;

    @Length(max = 7000, min = 20)
    private String description;

    @Future
    private LocalDateTime eventDate;

    private Long locationId;

    private boolean paid;

    private Integer participantLimit;

    private boolean requestModeration;

    @Length(max = 120, min = 3)
    private String title;
}
