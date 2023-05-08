package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.item.dto.ItemDtoShort;

import java.time.LocalDateTime;

@Getter
public class BookingDto {


    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private ItemDtoShort item;

    private BookerDtoShort booker;

    private BookingState status;

    public BookingDto(Long id, LocalDateTime start, LocalDateTime end, ItemDtoShort item, BookerDtoShort booker, BookingState status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = status;
    }
}
