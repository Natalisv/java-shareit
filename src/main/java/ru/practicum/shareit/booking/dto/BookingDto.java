package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemDtoShort;

import java.time.LocalDateTime;

@Getter
public class BookingDto {


    Long id;

    LocalDateTime start;

    LocalDateTime end;

    ItemDtoShort item;

    BookerDtoShort booker;

    Status status;

    public BookingDto(Long id, LocalDateTime start, LocalDateTime end, ItemDtoShort item, BookerDtoShort booker, Status status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = status;
    }
}
