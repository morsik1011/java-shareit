package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final String userIdHeader = "X-Sharer-User-Id";

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader(userIdHeader) Long ownerId,
                           @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен HTTP-запрос на добавление вещи пользователю с id: {}", ownerId);
        return itemService.add(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(userIdHeader) Long ownerId,
                              @PathVariable Long itemId,
                              @RequestBody ItemDto itemDto) {
        log.info("Получен HTTP-запрос на обновление вещи  с id: {}", itemId);
        return itemService.update(ownerId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBookings getItemByIdWithBooking(@RequestHeader(userIdHeader) Long ownerId,
                                                      @PathVariable Long itemId) {
        log.info("Получен HTTP-запрос на получении вещи по id: {}", itemId);
        return itemService.getByIdWithBooking(itemId);
    }

    @GetMapping
    public List<ItemDtoWithBookings> getAllItemsByOwnerWithBookings(
            @RequestHeader(userIdHeader) Long ownerId) {
        log.info("Получен HTTP-запрос на получение всех вещей пользователя с id: {}", ownerId);
        return itemService.getAllItemsByOwnerWithBookings(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(
            @RequestParam String text,
            @RequestHeader(userIdHeader) Long userId) {
        log.info("Получен HTTP-запрос на получение всех доступных вещей");
        return itemService.searchAvailableItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(userIdHeader) Long userId,
                                 @PathVariable Long itemId,
                                 @Valid @RequestBody CommentDto commentDto) {
        log.info("Получен HTTP-запрос на добавление комментария к вещи с id: {} от пользователя с id: {}",
                itemId, userId);
        return itemService.addComment(userId, itemId, commentDto);
    }
}
