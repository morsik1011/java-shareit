package ru.practicum.shareit.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.ApiError;
import ru.practicum.shareit.exceptions.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFound(UserNotFoundException exception) {
        return ApiError.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .description(exception.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleItemNotFound(ItemNotFoundException exception) {
        return ApiError.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .description(exception.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleAccess(AccessException exception) {
        return ApiError.builder()
                .errorCode(HttpStatus.FORBIDDEN.value())
                .description(exception.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleExistingEmails(ExistingEmailsException exception) {
        return ApiError.builder()
                .errorCode(HttpStatus.CONFLICT.value())
                .description(exception.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleBookingNotFound(BookingNotFoundException exception) {
        return ApiError.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .description(exception.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleItemAvailable(ItemAvailableException exception) {
        return ApiError.builder()
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .description(exception.getMessage())
                .build();
    }
}
