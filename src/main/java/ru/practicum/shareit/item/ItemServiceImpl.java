package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.AccessException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserService userService;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, UserService userService) {
        this.itemStorage = itemStorage;
        this.userService = userService;
    }

    @Override
    public ItemDto add(Long ownerId, ItemDto itemDto) {
        try {
            userService.getById(ownerId);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(String.format("Пользователь с id=%d не найден", ownerId));
        }

        Item item = ItemMapper.toItem(itemDto, ownerId);
        Item savedItem = itemStorage.create(item);
        return ItemMapper.toItemDto(savedItem);
    }


    @Override
    public ItemDto getById(Long itemId) {
        Item item = itemStorage.getById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(String.format("Вещь с id=%d не найдена", itemId)));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getAllItemsByOwner(Long ownerId) {
        return itemStorage.findAllByOwnerId(ownerId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchAvailableItems(String text) {
        List<Item> items = itemStorage.searchAvailableItems(text);
        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto update(Long ownerId, Long itemId, ItemDto itemDto) {
        Item existingItem = itemStorage.getById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(String.format("Вещь с id=%d не найдена", itemId)));

        if (!existingItem.getOwnerId().equals(ownerId)) {
            throw new AccessException("Редактировать вещь может только её владелец");
        }

        if (itemDto.getName() != null) {
            existingItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            existingItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }

        Item updatedItem = itemStorage.update(existingItem);
        return ItemMapper.toItemDto(updatedItem);
    }
}
