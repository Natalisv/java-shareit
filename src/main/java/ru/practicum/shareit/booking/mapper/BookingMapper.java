package ru.practicum.shareit.booking.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookerDtoShort;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
public class BookingMapper {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    public BookingDto toBookingDto(Booking booking) {
        Item item = itemRepository.findById(booking.getItemId()).get();
        ItemDtoShort itemDtoShort = new ItemDtoShort(item.getId(), item.getName());
        User user = userRepository.findById(booking.getBookerId()).get();
        BookerDtoShort bookerDtoShort = new BookerDtoShort(user.getId());

        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                itemDtoShort,
                bookerDtoShort,
                booking.getStatus()
        );
    }

    public Booking toBooking(BookingDto bookingDto) {
        return new Booking(
                bookingDto.getItem().getId(),
                bookingDto.getStart(),
                bookingDto.getEnd()
        );
    }
}
