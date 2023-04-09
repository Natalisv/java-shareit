package ru.practicum.shareit.booking.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Getter @Setter @ToString
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "start_date")
    LocalDateTime start;

    @Column(name = "end_date")
    LocalDateTime end;

    @Column(name = "item_id")
    Long itemId;

    @Column(name = "booker_id")
    Long bookerId;

    @Column
    @Enumerated
    Status status;

    public Booking(Long id, LocalDateTime startBooking, LocalDateTime end, Long itemId, Long bookerId, Status status) {
        this.id = id;
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
