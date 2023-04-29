package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.model.Booking;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "items")
@Getter
@Setter
@ToString
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String name;

    @Column
    String description;

    @Column
    Boolean available;

    @Column
    Long owner;

    @Column(name = "request_id")
    Long requestId;

    @OneToMany(
            targetEntity = Booking.class,
            mappedBy = "itemId",
            fetch = FetchType.EAGER
    )
    @JsonManagedReference
    private List<Booking> bookings;

    @OneToMany(
            targetEntity = Comment.class,
            mappedBy = "itemId",
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<Comment> comments;

    public Item(String name, String description, Boolean available, Long requestId) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }

    public Item() {
    }

    public Item(String name,String description,  Long id, Boolean available) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.available = available;
    }
}
