package ru.practicum.explore.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class NewUserRequestDto {

    @NotNull
    private String email;

    @NotNull
    private String name;
}
