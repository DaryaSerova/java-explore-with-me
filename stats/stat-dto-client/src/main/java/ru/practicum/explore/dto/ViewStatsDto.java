package ru.practicum.explore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ViewStatsDto {

    String app;

    String uri;

    Integer hits;
}
