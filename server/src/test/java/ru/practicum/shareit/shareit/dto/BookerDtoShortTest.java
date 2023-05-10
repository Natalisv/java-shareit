package ru.practicum.shareit.shareit.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookerDtoShort;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureJson
@AutoConfigureJsonTesters
class BookerDtoShortTest {

    @Autowired
    private JacksonTester<BookerDtoShort> json;

    @Test
    void bookingDtoTest() throws IOException {
        BookerDtoShort bookerDtoShort = new BookerDtoShort(1L);
        JsonContent<BookerDtoShort> result = json.write(bookerDtoShort);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }
}
