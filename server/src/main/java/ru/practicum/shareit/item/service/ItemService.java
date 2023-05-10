package ru.practicum.shareit.item.service;


import ru.practicum.shareit.exception.ExistException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface ItemService {

    ItemDto addItem(Long userId, ItemDto item);

    ItemDto getById(Long userId, Long itemId);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) throws ExistException;

    List<ItemDto> getUserItems(Long userId);

    List<ItemDto> findItem(Long userId, String text);

    Comment addComment(Long userId, Long itemId, Comment comment);
}
