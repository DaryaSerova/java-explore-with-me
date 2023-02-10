package ru.practicum.explore.category.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CategoryDto {

//    @NotNull
    private Long id;

    @NotNull
    private String name;
}
