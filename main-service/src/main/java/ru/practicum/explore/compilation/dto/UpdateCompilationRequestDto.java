package ru.practicum.explore.compilation.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UpdateCompilationRequestDto {

    private List<Long> events;

    private Boolean pinned;

    private String title;
}
