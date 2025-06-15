package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final String header = "X-Sharer-User-Id";

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader(header) Long ownerId,
                           @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен HTTP-запрос на добавление вещи пользователю с id: {}", ownerId);
        return itemService.add(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(header) Long ownerId,
                              @PathVariable Long itemId,
                              @RequestBody ItemDto itemDto) {
        log.info("Получен HTTP-запрос на обновление вещи  с id: {}", itemId);
        return itemService.update(ownerId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(header) Long ownerId,
                               @PathVariable Long itemId) {
        log.info("Получен HTTP-запрос на получении вещи по id: {}", itemId);
        return itemService.getById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByOwner(
            @RequestHeader(header) Long ownerId) {
        log.info("Получен HTTP-запрос на получение всех вещей пользователя с id: {}", ownerId);
        return itemService.getAllItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(
            @RequestParam String text,
            @RequestHeader(header) Long userId) {
        log.info("Получен HTTP-запрос на получение всех доступных вещей");
        return itemService.searchAvailableItems(text);
    }
}
