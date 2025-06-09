package ru.practicum.shareit.exceptions;

public class ExistingEmailsException extends RuntimeException {
    public ExistingEmailsException(String message) {
        super(message);
    }
}
