package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.dto.ItemDtoShort;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@ToString
public class ItemRequestDto {

    Long id;

    String description;

    Long requestorId;

    LocalDateTime created;

    List<ItemDtoShort> items;

    public ItemRequestDto(Long id, String description, Long requestorId, LocalDateTime created, List<ItemDtoShort> items) {
        this.id = id;
        this.description = description;
        this.requestorId = requestorId;
        this.created = created;
        this.items = items;
    }

    public ItemRequestDto(String description) {
        this.description = description;
    }

    public ItemRequestDto() {
    }
}
