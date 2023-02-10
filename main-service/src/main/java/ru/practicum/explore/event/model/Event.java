package ru.practicum.explore.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.explore.event.StateEvent;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "event_t")
public class Event {

    @NotNull
    @Length(max = 2000, min = 20)
    private String annotation;

    @NotNull
    @JoinColumn(name = "category_id")
    private Long categoryId;

    private Integer confirmedRequests;

    private LocalDateTime createdOn;

    @NotNull
    @Length(max = 7000, min = 20)
    private String description;

    @NotNull
    @Future
    private LocalDateTime eventDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long initiatorId;

    @NotNull
    private Long locationId;


    private boolean paid;

    private Integer participantLimit;

    private LocalDateTime publishedOn;

    private boolean requestModeration;

    private StateEvent state;

    @NotNull
    @Length(max = 120, min = 3)
    private String title;

    private Integer views;
}
