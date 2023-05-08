package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.Booking;


import javax.xml.stream.events.Comment;
import java.util.List;

@Getter
@Setter
public class ItemDto {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long requestId;
    private Long owner;

    public Booking lastBooking;

    public Booking nextBooking;
    public List<Comment> comments;

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

    public ItemDto(Long id, String name, String description, Boolean available, Long owner, List<Comment> comments,
                   Long requestId) {
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
