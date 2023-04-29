package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

@Getter
@Setter
public class ItemDto {

    Long id;

    String name;

    String description;

    Boolean available;

    Long requestId;
    Long owner;

    Booking lastBooking;

    Booking nextBooking;
    List<Comment> comments;

    public ItemDto() {
    }

    public ItemDto(Long id, String name, String description, Boolean available, Long owner, Booking lastBooking,
                   Booking nextBooking, List<Comment> comments, Long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
        this.comments = comments;
        this.requestId = requestId;
    }

    public ItemDto(Long id, String name, String description, Boolean available, Long owner, List<Comment> comments, Long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.comments = comments;
        this.requestId = requestId;
    }

    public ItemDto(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }

    public ItemDto(String description, Boolean available) {
        this.description = description;
        this.available = available;
    }
}
