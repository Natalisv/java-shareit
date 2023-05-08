package ru.practicum.shareit.shareit.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureJson
@AutoConfigureJsonTesters
class BookingDtoTest {

    LocalDateTime start = LocalDateTime.of(2024, 04, 27, 10, 00);
    LocalDateTime end = LocalDateTime.of(2024, 04, 28, 10, 00);

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void bookingDtoTest() throws IOException {
        BookingDto bookingDto = new BookingDto(
                1L,
                start,
                end,
                null,
                null,
                BookingState.APPROVED
        );

        JsonContent<BookingDto> result = json.write(bookingDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2024-04-27T10:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2024-04-28T10:00:00");
    }
}
