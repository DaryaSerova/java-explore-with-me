package ru.practicum.explore.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
public class ViewStatsDto {

    String app;

    String uri;

    Integer hits;
}
