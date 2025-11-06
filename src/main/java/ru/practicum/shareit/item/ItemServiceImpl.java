package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.AccessException;
import ru.practicum.shareit.exceptions.ItemAvailableException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookings;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public ItemDto add(Long ownerId, ItemDto itemDto) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException
                        (String.format("Пользователь с id=%d не найден", ownerId)));

        Item item = ItemMapper.toItem(itemDto, ownerId);
        Item savedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public List<ItemDto> searchAvailableItems(String text) {
        if (text == null || text.trim().isEmpty()) {
            return List.of();
        }
        List<Item> items = itemRepository.searchAvailableItems(text);
        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto update(Long ownerId, Long itemId, ItemDto itemDto) {

        Item existingItem = itemRepository.findById(itemId)
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

        Item updatedItem = itemRepository.save(existingItem);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    @Transactional
    public ItemDtoWithBookings getByIdWithBooking(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException
                        (String.format("Вещь с id=%d не найдена", itemId)));

        List<CommentDto> comments = CommentMapper.toListDto(commentRepository.findByItemId(itemId));

        LocalDateTime now = LocalDateTime.now();


        Booking nextBooking = bookingRepository.findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(
                itemId, now, BookingStatus.APPROVED);

        Booking lastBooking = bookingRepository.findFirstByItemIdAndEndBeforeAndStatusOrderByEndDesc(
                itemId, now, BookingStatus.CANCELED);

        return ItemMapper.toItemDtoWithBookings(item, comments,
                nextBooking != null ? nextBooking.getStart() : null,
                lastBooking != null ? lastBooking.getEnd() : null);
    }

    @Override
    @Transactional
    public List<ItemDtoWithBookings> getAllItemsByOwnerWithBookings(Long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new UserNotFoundException(String.format("Пользователь с id=%d не найден", ownerId));
        }

        List<Item> items = itemRepository.findByOwnerId(ownerId);
        return items.stream()
                .map(item -> {
                    List<CommentDto> comments = CommentMapper.toListDto(commentRepository.findByItemId(item.getId()));

                    Booking nextBooking = bookingRepository.findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(
                            item.getId(), LocalDateTime.now(), BookingStatus.APPROVED);

                    Booking lastBooking = bookingRepository.findFirstByItemIdAndEndBeforeAndStatusOrderByEndDesc(
                            item.getId(), LocalDateTime.now(), BookingStatus.APPROVED);

                    return ItemMapper.toItemDtoWithBookings(item, comments,
                            nextBooking != null ? nextBooking.getStart() : null,
                            lastBooking != null ? lastBooking.getEnd() : null);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("Пользователь с id=%d не найден", userId)));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(
                        String.format("Вещь с id=%d не найдена", itemId)));


        List<Booking> userBookings = bookingRepository.findByBookerIdAndItemIdAndEndBeforeAndStatus(
                userId, itemId, LocalDateTime.now(), BookingStatus.APPROVED);

        if (userBookings.isEmpty()) {
            throw new ItemAvailableException("Пользователь не брал эту вещь в аренду или аренда еще не завершена");
        }

        Comment comment = CommentMapper.toComment(commentDto, item, author);
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toDto(savedComment);
    }

}
