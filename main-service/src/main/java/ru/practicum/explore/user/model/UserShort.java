package ru.practicum.explore.user.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserShort {

    private Long id;

    private String name;

}
