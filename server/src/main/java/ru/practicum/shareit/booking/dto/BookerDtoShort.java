package ru.practicum.shareit.booking.dto;

import lombok.Getter;

@Getter
public class BookerDtoShort {

    private Long id;

    public BookerDtoShort(Long id) {
        this.id = id;
    }
}
