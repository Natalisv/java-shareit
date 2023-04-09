package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class ItemRepositoryImpl implements ItemRepository {

    Map<Long, List<Item>> items = new HashMap<>();

    @Override
    public void addItem(Long userId, List<Item> item) {
        items.put(userId, item);
    }

    @Override
    public List<Item> getUserItems(Long userId) {
        return items.get(userId);
    }
}
