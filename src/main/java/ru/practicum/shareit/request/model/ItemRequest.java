package ru.practicum.shareit.request.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "requests")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String description;

    @Column(name = "requestor_id")
    private Long requestorId;

    @Column
    private LocalDateTime created;

    public ItemRequest(String description) {
        this.description = description;
    }

    public ItemRequest() {
    }

}
