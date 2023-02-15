package ru.practicum.explore.location.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class LocationDto {

    private Long id;

    private Float lat;

    private Float lon;
}
