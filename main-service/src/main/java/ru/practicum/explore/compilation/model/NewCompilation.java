package ru.practicum.explore.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class NewCompilation {

    private Long eventId;

    private Boolean pinned;

    private String title;
}
