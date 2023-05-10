package ru.practicum.shareit.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class ItemControllerTest {

    protected ItemDto itemDto;

    protected Comment comment;

    @MockBean
    ItemServiceImpl itemService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void createItem() {
        itemDto = new ItemDto(
                1L,
                "Дрель",
                "Простая дрель",
                true,
                10L,
                Collections.emptyList(),
                2L
        );
        comment = new Comment(
                5L,
                "comment",
                1L,
                2L
        );
    }

    @Test
    void addItem() throws Exception {
        when(itemService.addItem(any(), any()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Дрель")))
                .andExpect(jsonPath("$.description", Matchers.is("Простая дрель")));
    }

    @Test
    void update() throws Exception {
        when(itemService.updateItem(any(), any(), any()))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Дрель")))
                .andExpect(jsonPath("$.description", Matchers.is("Простая дрель")));
    }

    @Test
    void getById() throws Exception {
        when(itemService.getById(any(), any()))
                .thenReturn(itemDto);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Дрель")))
                .andExpect(jsonPath("$.description", Matchers.is("Простая дрель")))
                .andExpect(jsonPath("$.available", Matchers.is(Boolean.TRUE)));
    }

    @Test
    void getUserItems() throws Exception {
        when(itemService.getUserItems(any()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Дрель")))
                .andExpect(jsonPath("$[0].description", Matchers.is("Простая дрель")))
                .andExpect(jsonPath("$[0].available", Matchers.is(Boolean.TRUE)));
    }

    @Test
    void search() throws Exception {
        when(itemService.findItem(any(), any()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "дрель")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Дрель")))
                .andExpect(jsonPath("$[0].description", Matchers.is("Простая дрель")))
                .andExpect(jsonPath("$[0].available", Matchers.is(Boolean.TRUE)));
    }

    @Test
    void addComment() throws Exception {
        when(itemService.addComment(any(), any(), any()))
                .thenReturn(comment);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(comment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(5)))
                .andExpect(jsonPath("$.text", Matchers.is("comment")));
    }
}
