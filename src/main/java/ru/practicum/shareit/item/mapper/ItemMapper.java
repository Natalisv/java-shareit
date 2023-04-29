package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {

    private ItemMapper() {
        throw new RuntimeException();
    }

    public static ItemDto toItemDto(Item item) {
        return getToItemDto(item);
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getRequestId()
        );
    }

    public static ItemDtoShort toItemDtoShort(Item item) {
        return new ItemDtoShort(
                item.getId(),
                item.getName(),
                item.getOwner(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequestId()
        );
    }

    public static ItemDto toItemDtoBooking(Item item) {
        List<Booking> bookings = item.getBookings();
        if (bookings == null || bookings.size() == 0) {
            return getToItemDto(item);
        }
        bookings = bookings.stream()
                .filter(b -> !b.getStatus().equals(Status.REJECTED))
                .sorted(Comparator.comparing(Booking::getStart)).collect(Collectors.toList());
        Booking lastBooking = null;
        Booking nextBooking = null;
        if (bookings.size() == 0) {
            return getToItemDto(item);
        } else if (bookings.size() == 1) {
            if (bookings.get(0).getStatus().equals(Status.APPROVED) || bookings.get(0).getEnd().isBefore(LocalDateTime.now())) {
                lastBooking = bookings.get(0);
            } else {
                nextBooking = bookings.get(0);
            }
        } else {
            lastBooking = bookings.stream().filter(b -> b.getEnd().isBefore(LocalDateTime.now())).reduce((first, second) -> second).get();
            nextBooking = bookings.stream().filter(b -> b.getStart().isAfter(LocalDateTime.now())).findFirst().get();
        }

        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                lastBooking,
                nextBooking,
                item.getComments(),
                item.getRequestId()
        );
    }

    private static ItemDto getToItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getComments(),
                item.getRequestId()
        );
    }
}
