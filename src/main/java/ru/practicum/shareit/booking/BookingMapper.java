package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;

@RequiredArgsConstructor
public class BookingMapper {
    public static Booking toBooking(BookingRequestDto bookingDto, User user, Item item) {
        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setStatus(BookingStatus.WAITING);
        return booking;
    }

    public static BookingResponseDto toBookingResponseDto(Booking booking) {
        return new BookingResponseDto(
                booking.getId(),
                ItemMapper.toItemDto(booking.getItem()),
                UserMapper.toUserDto(booking.getBooker()),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus()
        );
    }
}
