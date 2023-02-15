package ru.practicum.explore.category.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NewCategoryDto {

    @NotNull
    private String name;
}
