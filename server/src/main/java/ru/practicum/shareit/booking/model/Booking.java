package ru.practicum.shareit.booking.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.BookingState;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Getter
@Setter
@ToString
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date")
    private LocalDateTime start;

    @Column(name = "end_date")
    private LocalDateTime end;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "booker_id")
    private Long bookerId;

    @Column
    @Enumerated
    private BookingState status;

    public Booking(LocalDateTime startBooking, LocalDateTime end, Long itemId, Long bookerId, BookingState status) {
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
