package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.util.Date;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {

    Long id;

    Date start;

    Date end;

    Item item;

    Long booker;

    Status status;

}
