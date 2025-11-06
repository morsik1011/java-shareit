package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingResponseDto create(
            @RequestBody BookingRequestDto bookingRequestDto,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен HTTP-запрос на создание бронирования для пользователя с id {}", userId);
        return bookingService.create(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approve(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam boolean approved,
            @PathVariable Long bookingId) {
        log.info("Получен HTTP-запрос на изменение статуса бронирования c id {} от владельца c id {} на {}", bookingId, userId, approved);
        return bookingService.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@PathVariable Long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен HTTP-запрос на получение бронирования по id {}", bookingId);
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public List<BookingResponseDto> getUserBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получен HTTP-запрос на получение списка бронирований пользователя {} со статусом вещей {}",
                userId, state);
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получен HTTP-запрос на получение списка бронирований владельца {} со статусом вещей {}",
                userId, state);
        return bookingService.getOwnerBookings(userId, state);
    }
}
