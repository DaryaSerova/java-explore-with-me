package ru.practicum.explore.participation.dto;


import lombok.*;
import ru.practicum.explore.participation.ParticipationStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EventRequestStatusUpdateRequestDto {

    private List<Long> requestIds;

    private ParticipationStatus status;
}
