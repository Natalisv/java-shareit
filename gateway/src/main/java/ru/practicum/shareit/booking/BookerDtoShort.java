package ru.practicum.shareit.booking;

import lombok.Getter;

@Getter
public class BookerDtoShort {

    private Long id;

    public BookerDtoShort(Long id) {
        this.id = id;
    }
}
