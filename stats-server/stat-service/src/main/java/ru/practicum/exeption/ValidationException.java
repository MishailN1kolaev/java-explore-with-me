package ru.practicum.exeption;

public class ValidationException extends RuntimeException {
    public ValidationException(String s) {
        super(s);
    }
}
