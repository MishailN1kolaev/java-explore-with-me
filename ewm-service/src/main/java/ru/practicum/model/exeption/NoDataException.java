package ru.practicum.model.exeption;

public class NoDataException extends RuntimeException {
    public NoDataException(String message) {
        super(message);
    }
}