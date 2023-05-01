package ru.practicum.shareit.booking.dto;

import lombok.Getter;

@Getter
public class BookerDtoShort {
    Long id;

    public BookerDtoShort(Long id) {
        this.id = id;
    }
}
