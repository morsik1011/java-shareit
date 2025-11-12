package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;

import java.util.List;

public interface ItemService {
    ItemDto add(Long ownerId, ItemDto itemDto);

    List<ItemDto> searchAvailableItems(String text);

    ItemDto update(Long ownerId, Long itemId, ItemDto itemDto);

    ItemDtoWithBookings getByIdWithBooking(Long itemId);

    List<ItemDtoWithBookings> getAllItemsByOwnerWithBookings(Long ownerId);

    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);
}
