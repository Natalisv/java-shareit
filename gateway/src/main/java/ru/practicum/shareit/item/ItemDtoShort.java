package ru.practicum.shareit.item;

import lombok.Data;

@Data
public class ItemDtoShort {

    private Long id;

    private String name;
    private Long owner;

    private String description;
    private Boolean available;

    private Long requestId;

    public ItemDtoShort(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ItemDtoShort(Long id, String name, Long owner, String description, Boolean available, Long requestId) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }
}
