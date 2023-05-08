package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.ItemDtoShort;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@ToString
public class ItemRequestDto {

    private Long id;

    private String description;

    private Long requestorId;

    private LocalDateTime created;

    private List<ItemDtoShort> items;

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
