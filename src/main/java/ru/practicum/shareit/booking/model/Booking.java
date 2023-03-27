package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;

import java.util.Date;

@Data
public class Booking {

    Long id;

    Date start;

    Date end;

    Item item;

    Long booker;

    Status status;

}
