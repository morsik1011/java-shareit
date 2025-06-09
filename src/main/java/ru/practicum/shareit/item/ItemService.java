package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto add(Long ownerId, ItemDto itemDto);

    ItemDto getById(Long itemId);

    List<ItemDto> getAllItemsByOwner(Long ownerId);

    List<ItemDto> searchAvailableItems(String text);

    ItemDto update(Long ownerId, Long itemId, ItemDto itemDto);
}
