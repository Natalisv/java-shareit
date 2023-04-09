package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ExistException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.CommentsRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CommentsRepository commentsRepository;

    @Transactional
    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        if (isValid(itemDto)) {
            if (userId != null && isContainsUser(userId)) {
                Item item = ItemMapper.toItem(itemDto);
                item.setOwner(userId);
                Item savedItem = itemRepository.save(item);
                return ItemMapper.toItemDto(savedItem);
            }
            log.error("Пользователя с userId = " + userId + " не существует");
            throw new IllegalArgumentException();
        }
        log.error("Невалидная сущность");
        throw new ValidationException("Невалидная сущность");
    }

    @Transactional
    @Override
    public ItemDto getById(Long userId, Long itemId) {
        if (isContainsUser(userId)) {
            Item item = itemRepository.findById(itemId).orElseThrow(() -> {
                log.error("Сущность с id = " + itemId + " не найдена");
                throw new IllegalArgumentException();
            });
            if (!item.getOwner().equals(userId)) {
                return ItemMapper.toItemDto(item);
            } else {
                return ItemMapper.toItemDtoBooking(item);
            }
        } else {
            log.error("Пользователя с userId = " + userId + " не существует");
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) throws ExistException {
        if (userId != null && isContainsUser(userId)) {
            try {
                Item item = itemRepository.findByIdAndOwner(itemId, userId);
            } catch (Exception e) {
                throw new ValidationException(e.getMessage());
            }
            if (itemDto.getName() != null) {
                itemRepository.updateItemName(itemDto.getName(), itemId);
            }
            if (itemDto.getDescription() != null) {
                itemRepository.updateItemDescription(itemDto.getDescription(), itemId);
            }
            if (itemDto.getAvailable() != null) {
                itemRepository.updateItemAvailable(itemDto.getAvailable(), itemId);
            }
            Item savedItem = itemRepository.findById(itemId).get();
            return ItemMapper.toItemDto(savedItem);

        }
        log.error("Пользователя с userId = " + userId + " не существует");
        throw new ExistException("Пользователя с userId = " + userId + " не существует");
    }

    @Transactional
    @Override
    public List<ItemDto> getUserItems(Long userId) {
        if (isContainsUser(userId)) {
            List<Item> items = itemRepository.findByOwner(userId);
            return items.stream().map(ItemMapper::toItemDtoBooking).collect(Collectors.toList());
        }
        return null;
    }

    @Transactional
    @Override
    public List<ItemDto> findItem(String text) {
        if (text != null && !text.isEmpty() && !text.isBlank()) {
            List<Item> items = itemRepository.findAll();
            List<ItemDto> findItems = new ArrayList<>();
            items.stream()
                    .filter(Item::getAvailable)
                    .filter(i -> i.getName().toLowerCase().trim().contains(text.toLowerCase().trim()) ||
                            i.getDescription().toLowerCase().trim().contains(text.toLowerCase().toLowerCase().trim()))
                    .forEach(i -> findItems.add(ItemMapper.toItemDto(i)));
            return findItems;
        }
        log.info("Сущность не найдена");
        return Collections.emptyList();
    }

    @Transactional
    public Comment addComment(Long userId, Long itemId, Comment comment) {
        if (isContainsUser(userId)) {
            if (!comment.getText().isEmpty() && !comment.getText().isBlank()) {
                List<Booking> bookings = bookingRepository.findByBookerIdAndItemId(userId, itemId);
                if (bookings != null) {
                    if (bookings.stream().anyMatch(b -> b.getStatus().equals(Status.APPROVED)
                            && b.getStart().isBefore(LocalDateTime.now())
                            && b.getEnd().isBefore(LocalDateTime.now()))) {
                        comment.setItemId(itemId);
                        comment.setAuthorId(userId);
                        String userName = userRepository.getReferenceById(userId).getName();
                        comment.setAuthorName(userName);
                        comment.setCreated(LocalDateTime.now());
                        return commentsRepository.save(comment);

                       /* Comment savedComment = commentsRepository.save(comment);
                        return CommentMapper.toCommentDto(savedComment, userName);*/
                    } else {
                        log.error("Не возможно задать комментарий бронированию");
                        throw new ValidationException("Не возможно задать комментарий бронированию");
                    }
                } else {
                    log.error("У пользователя = " + userId + " нет бронирований");
                    throw new ValidationException("У пользователя = " + userId + " нет бронирований");
                }
            } else {
                log.error("Комментарий не задан");
                throw new ValidationException("Задайте комментарий");
            }
        } else {
            log.error("Пользователя с userId = " + userId + " не существует");
            throw new IllegalArgumentException();
        }
    }

    private boolean isContainsUser(Long id) {
        return userRepository.existsById(id);
    }

    private boolean isValid(ItemDto item) {
        return (item.getName() != null && !item.getName().isEmpty() && item.getDescription() != null && !item.getDescription().isEmpty() && item.getAvailable() != null);
    }
}
