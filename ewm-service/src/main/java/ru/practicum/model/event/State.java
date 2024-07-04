package ru.practicum.model.event;

import java.util.Optional;

public enum State {
    PENDING,
    PUBLISHED,
    CANCELED,
    PUBLISH_EVENT,
    CANCEL_REVIEW,
    SEND_TO_REVIEW,
    REJECT_EVENT;

    public static Optional<State> from(String stringState) {
        for (State state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }

}