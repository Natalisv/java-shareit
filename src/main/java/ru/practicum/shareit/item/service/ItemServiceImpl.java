package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ExistException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    @Autowired
    private final ItemRepository itemRepository;
    @Autowired
    private final UserRepository userRepository;

    private Long startId = 1L;


    @Override
    public ItemDto addItem(Long userId, ItemDto itemDto) {
        if (isValid(itemDto)) {
            if (userId != null && isContainsUser(userId)) {
                Item item = ItemMapper.toItem(itemDto);
                item.setId(startId);
                item.setOwner(userId);
                List<Item> itemsList = itemRepository.getUserItems(userId);
                if (itemsList == null) {
                    itemsList = new ArrayList<>();
                }
                itemsList.add(item);
                itemRepository.addItem(userId, itemsList);
                startId++;
                Item savedItem = itemRepository.getUserItems(userId).stream().filter(i -> i.getId().equals(item.getId())).findFirst().get();
                return ItemMapper.toItemDto(savedItem);
            }
            log.error("Пользователя с userId = " + userId + " не существует");
            throw new IllegalArgumentException();
        }
        log.error("Невалидная сущность");
        throw new ValidationException();
    }

    @Override
    public ItemDto getById(Long userId, Long itemId) {
        List<User> users = userRepository.getUsers();
        List<Long> usersId = users.stream().map(User::getId).collect(Collectors.toList());
        for (Long id : usersId) {
            Item savedItem = itemRepository.getUserItems(id).stream().filter(i -> i.getId().equals(id)).findFirst().orElse(null);
            if (savedItem != null) {
                return ItemMapper.toItemDto(savedItem);
            }
        }
        log.error("Сущность с id = " + itemId + " не найдена");
        throw new ValidationException();
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) throws ExistException {
        if (userId != null && isContainsUser(userId)) {
            List<Item> userItems = itemRepository.getUserItems(userId);
            if (userItems != null) {
                Item item = userItems.stream().filter(i -> i.getId().equals(itemId)).findFirst().orElse(null);
                if (item != null) {
                    Item finalItem = this.updateItem(item, itemDto);
                    userItems.removeIf(i -> i.getId().equals(finalItem.getId()));
                    userItems.add(finalItem);
                    Item savedItem = itemRepository.getUserItems(userId).stream().filter(i -> i.getId().equals(finalItem.getId())).findFirst().get();
                    return ItemMapper.toItemDto(savedItem);
                }
            } else {
                log.error("Не найдена сущность с id = " + itemId);
                throw new IllegalArgumentException();
            }
        }
        log.error("Пользователя с userId = " + userId + " не существует");
        throw new ExistException("Пользователя с userId = " + userId + " не существует");
    }

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        List<Item> items = itemRepository.getUserItems(userId);
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findItem(String text) {
        if (text != null && !text.isEmpty() && !text.isBlank()) {
            List<User> users = userRepository.getUsers();
            List<Long> usersId = users.stream().map(User::getId).collect(Collectors.toList());
            List<ItemDto> response = new ArrayList<>();
            for (Long id : usersId) {
                List<Item> userItems = itemRepository.getUserItems(id);
                userItems.stream()
                        .filter(Item::getAvailable)
                        .filter(i -> i.getName().toLowerCase().trim().contains(text.toLowerCase().trim()) ||
                                i.getDescription().toLowerCase().trim().contains(text.toLowerCase().toLowerCase().trim()))
                        .forEach(item -> response.add(ItemMapper.toItemDto(item)));
            }
            return response;
        }
        log.info("Сущность не найдена");
        return Collections.emptyList();
    }

    private boolean isContainsUser(Long id) {
        List<UserDto> list = new ArrayList<>();
        userRepository.getUsers().forEach(u -> list.add(UserMapper.toUserDto(u)));
        return list.stream().anyMatch(u -> u.getId().equals(id));
    }

    private boolean isValid(ItemDto item) {
        return (item.getName() != null && !item.getName().isEmpty() && item.getDescription() != null && !item.getDescription().isEmpty() && item.getAvailable() != null);
    }

    private Item updateItem(Item item, ItemDto itemDto) {
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return item;
    }

}
