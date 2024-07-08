package ru.practicum.model.exeption;

public class ValidationException extends RuntimeException {
    public ValidationException(String s) {
        super(s);
    }
}
