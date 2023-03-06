package ru.practicum.explore.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ViewStatsDto {

    String app;

    String uri;

    Integer hits;
}
