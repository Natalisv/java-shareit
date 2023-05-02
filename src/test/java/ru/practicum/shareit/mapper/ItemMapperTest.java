package ru.practicum.shareit.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureJson
class ItemMapperTest {

    protected Item item;
    protected Booking booking;
    LocalDateTime start = LocalDateTime.of(2024, 04, 27, 10, 00);
    LocalDateTime end = LocalDateTime.of(2024, 04, 28, 10, 00);

    @BeforeEach
    void created() {
        item = new Item(
                1L,
                "name",
                "description",
                Boolean.TRUE,
                2L,
                3L
        );
        booking = new Booking(
                3L,
                start,
                end
        );
    }

    @Test
    void toItemDtoShort() {
        ItemDtoShort result = ItemMapper.toItemDtoShort(item);
        assertNotNull(result);
        assertEquals(result.getId(), item.getId());
        assertEquals(result.getName(), item.getName());
    }

    @Test
    void toItemDtoBooking() {
        item.setBookings(List.of(booking));
        booking.setStatus(Status.WAITING);
        ItemDto result = ItemMapper.toItemDtoBooking(item);
        assertNotNull(result);
        assertEquals(result.getId(), item.getId());
        assertEquals(result.getName(), item.getName());
    }
}
