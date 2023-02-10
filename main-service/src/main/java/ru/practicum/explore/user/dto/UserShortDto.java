package ru.practicum.explore.user.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserShortDto {

    private Long id;

    private String name;

}
