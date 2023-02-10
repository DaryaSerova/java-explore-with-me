package ru.practicum.explore.compilation.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CompilationDto {

    private Long eventId;

    private Long id;

    private Boolean pinned;

    private String title;
}
