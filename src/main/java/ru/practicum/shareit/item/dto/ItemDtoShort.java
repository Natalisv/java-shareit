package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemDtoShort {
    Long id;
    String name;

    public ItemDtoShort(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
