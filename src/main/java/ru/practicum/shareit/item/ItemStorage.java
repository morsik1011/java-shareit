package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Item create(Item item);

    List<Item> findAllByOwnerId(Long ownerId);

    Optional<Item> getById(Long id);

    List<Item> searchAvailableItems(String text);

    Item update(Item item);
}
