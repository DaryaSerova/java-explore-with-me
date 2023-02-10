package ru.practicum.explore.participation.dto;

import lombok.*;
import ru.practicum.explore.participation.ParticipationStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ParticipationRequestDto {

    private Long id;

    private LocalDateTime created;

    private Long eventId;

    private Long requesterId;

    private ParticipationStatus status;
}
