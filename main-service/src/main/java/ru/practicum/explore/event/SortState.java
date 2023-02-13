package ru.practicum.explore.event;

public enum SortState {

    EVENT_DATE("eventDate"),
    VIEWS("views");

    private String field;

    SortState(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
