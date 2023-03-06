package ru.practicum.explore.comment.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NewCommentDto {

    @NotNull
    private String content;
}
