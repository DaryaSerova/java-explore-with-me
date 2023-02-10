package ru.practicum.explore.compilation.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class NewCompilationDto {

    private Long eventId;

    private Boolean pinned;

    private String title;
}
