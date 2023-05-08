package ru.practicum.shareit.booking;

import ch.qos.logback.core.status.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Booking {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long itemId;

    private Long bookerId;

    private Status status;

    public Booking(LocalDateTime startBooking, LocalDateTime end, Long itemId, Long bookerId, Status status) {
        this.start = startBooking;
        this.end = end;
        this.itemId = itemId;
        this.bookerId = bookerId;
        this.status = status;
    }

    public Booking(Long itemId, LocalDateTime startBooking, LocalDateTime end) {
        this.start = startBooking;
        this.end = end;
        this.itemId = itemId;
    }

    public Booking() {
    }
}
