package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;


/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {

    Long id;

    String name;

    String description;

    Boolean available;

    Long owner;

    ItemRequest itemRequest;

    public Item(Long id, String name, String description, Boolean available, Long owner, ItemRequest itemRequest) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.itemRequest = itemRequest;
    }

    public Item(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
