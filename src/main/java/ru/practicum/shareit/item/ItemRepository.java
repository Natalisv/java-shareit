package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    void addItem(Long userId, List<Item> item);

    List<Item> getUserItems(Long userId);

}
