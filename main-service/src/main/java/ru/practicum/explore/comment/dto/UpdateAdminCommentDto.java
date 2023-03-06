package ru.practicum.explore.comment.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.explore.admin.StateAction;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UpdateAdminCommentDto {

    @Length(max = 3000, min = 20)
    private String content;

    @Enumerated(EnumType.STRING)
    private StateAction stateAction;
}
