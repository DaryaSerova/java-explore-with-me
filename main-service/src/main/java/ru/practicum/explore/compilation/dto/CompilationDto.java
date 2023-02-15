package ru.practicum.explore.compilation.dto;

import lombok.*;
import ru.practicum.explore.event.dto.EventShortDto;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CompilationDto {

    private List<EventShortDto> events;

    @NotNull
    private Long id;

    @NotNull
    private Boolean pinned;

    @NotNull
    private String title;
}
