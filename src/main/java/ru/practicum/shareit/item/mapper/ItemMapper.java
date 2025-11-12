package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        Long requestId = null;
        if (item.getRequest() != null) {
            requestId = item.getRequest().getId();
        }
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(requestId)
                .build();
    }

    public static Item toItem(ItemDto itemDto, Long ownerId) {
        ItemRequest request = null;
        if (itemDto.getRequestId() != null) {
            request = new ItemRequest();
            request.setId(itemDto.getRequestId());
        }

        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                ownerId,
                request
        );
    }

    public static ItemDtoWithBookings toItemDtoWithBookings(Item item, List<CommentDto> comments, LocalDateTime next, LocalDateTime last) {
        return ItemDtoWithBookings.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .nextBooking(next)
                .lastBooking(last)
                .comments(comments != null ? comments : List.of())
                .build();
    }
}
