package ru.practicum.explore.event.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.explore.admin.StateAction;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventAdminRequestDto {

    @Length(max = 2000, min = 20)
    private String annotation;

    private Long categoryId;

    @Length(max = 7000, min = 20)
    private String description;

    private LocalDateTime eventDate;

    private Long locationId;

    private boolean paid;

    private Integer participantLimit;

    private boolean requestModeration;

    private StateAction stateAction;

    @Length(max = 120, min = 3)
    private String title;
}
