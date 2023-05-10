package ru.practicum.shareit.booking;

import ch.qos.logback.core.status.Status;
import lombok.Getter;
import ru.practicum.shareit.item.ItemDtoShort;

import java.time.LocalDateTime;

@Getter
public class BookingDto {


    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private ItemDtoShort item;

    private BookerDtoShort booker;

    private Status status;

    public BookingDto(Long id, LocalDateTime start, LocalDateTime end, ItemDtoShort item, BookerDtoShort booker, Status status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = status;
    }

    public BookingDto() {
    }
}
