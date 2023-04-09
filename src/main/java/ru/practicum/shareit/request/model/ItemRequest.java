package ru.practicum.shareit.request.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "requests")
public class ItemRequest {

    @Id
    Long id;

    @Column
    String description;

    @Column(name = "requestor_id")
    Long requestor;

    @Column
    Date created;
}
