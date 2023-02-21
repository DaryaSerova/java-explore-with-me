package ru.practicum.explore.event.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import ru.practicum.explore.event.StateEvent;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.Event_;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.explore.event.DateFormatConstant.DATE_FORMAT;
import static ru.practicum.explore.event.repository.specification.SpecificationUtils.*;

public class EventSpecification {

    public static Specification<Event> requestSpec(List<Long> users, List<StateEvent> states, List<Long> categories,
                                                   String rangeStart, String rangeEnd) {
        List<Specification<Event>> specifications = new ArrayList<>();

        if (!CollectionUtils.isEmpty(users)) {
            specifications.add(in(Event_.initiatorId, users));
        }
        if (!CollectionUtils.isEmpty(states)) {
            specifications.add(in(Event_.state, states));
        }

        if (!CollectionUtils.isEmpty(categories)) {
            specifications.add(in(Event_.categoryId, categories));
        }

        if (rangeStart != null) {
            LocalDateTime start = LocalDateTime.parse(URLDecoder.decode(rangeStart, StandardCharsets.UTF_8),
                    DateTimeFormatter.ofPattern((DATE_FORMAT)));
            specifications.add(greaterThanOrEqualTo(Event_.eventDate, start));
        }

        if (rangeEnd != null) {
            LocalDateTime end = LocalDateTime.parse(URLDecoder.decode(rangeEnd, StandardCharsets.UTF_8),
                    DateTimeFormatter.ofPattern((DATE_FORMAT)));
            specifications.add(lessThanOrEqualTo(Event_.eventDate, end));
        }
        return and(specifications);
    }

    public static Specification<Event> requestSpec(String text, List<Long> categories, Boolean paid,
                                                   String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                   StateEvent state) {
        List<Specification<Event>> specifications = new ArrayList<>();
        if (!CollectionUtils.isEmpty(categories)) {
            specifications.add(in(Event_.categoryId, categories));
        }
        if (rangeStart != null) {
            LocalDateTime start = LocalDateTime.parse(URLDecoder.decode(rangeStart, StandardCharsets.UTF_8),
                    DateTimeFormatter.ofPattern((DATE_FORMAT)));
            specifications.add(greaterThanOrEqualTo(Event_.eventDate, start));
        } else {
            specifications.add(greaterThanOrEqualTo(Event_.eventDate, LocalDateTime.now()));
        }

        if (rangeEnd != null) {
            LocalDateTime end = LocalDateTime.parse(URLDecoder.decode(rangeEnd, StandardCharsets.UTF_8),
                    DateTimeFormatter.ofPattern((DATE_FORMAT)));
            specifications.add(lessThanOrEqualTo(Event_.eventDate, end));
        }

        if (paid != null) {
            specifications.add(isEquals(Event_.paid, paid));
        }

        if (onlyAvailable != null && onlyAvailable) {
            specifications.add(lessThan(Event_.confirmedRequests, Event_.participantLimit));
        }

        if (!StringUtils.isEmpty(text)) {
            specifications.add(or(List.of(containsIgnoreCase(Event_.annotation, text),
                    containsIgnoreCase(Event_.description, text))));

        }

        if (state != null) {
            specifications.add(isEquals(Event_.state, state));
        }

        return and(specifications);
    }
}
