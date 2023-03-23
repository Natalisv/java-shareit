package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemDto {

    Long id;

    String name;

    String description;

    Boolean available;

    Long itemRequest;

    public ItemDto(Long id, String name, String description, Boolean available, Long itemRequest) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.itemRequest = itemRequest;
    }


}
