package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookerDtoShort {
    Long id;

    public BookerDtoShort(Long id) {
        this.id = id;
    }
}