package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public Item create(Item item) {
        item.setId(idCounter++);
        items.put(item.getId(), item);
        return item;
    }


    @Override
    public Optional<Item> getById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> findAllByOwnerId(Long ownerId) {
        return items.values().stream()
                .filter(item -> ownerId.equals(item.getOwnerId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchAvailableItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        String searchText = text.trim().toLowerCase();
        return items.values().stream()
                .filter(Item::isAvailableForBooking)
                .filter(item ->
                        item.getName() != null && item.getName().toLowerCase().contains(searchText) ||
                                item.getDescription() != null && item.getDescription().toLowerCase().contains(searchText)
                )
                .collect(Collectors.toList());
    }


    @Override
    public Item update(Item item) {
        if (!items.containsKey(item.getId())) {
            throw new ItemNotFoundException("Вещь не найдена");
        }
        items.put(item.getId(), item);
        return item;
    }
}