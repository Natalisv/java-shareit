package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.exception.ExistException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
public class ItemServiceImplTest {

    protected ItemDto itemDto;
    protected ItemDto itemDtoNotValid;

    protected User user;

    @Autowired
    ItemServiceImpl itemService;

    @Autowired
    UserRepository userRepository;


    @BeforeEach
    void createItem() {
        itemDto = new ItemDto(
                "Дрель",
                "Простая дрель",
                true
        );

        itemDtoNotValid = new ItemDto(
                "Простая дрель",
                true
        );

        user = new User(
                "user",
                "user@user.com"
        );
    }

    @Test
    void addItem() {
        User savedUser = userRepository.save(user);
        ItemDto result = itemService.addItem(savedUser.getId(), itemDto);
        assertNotNull(result);
        assertEquals(result.getOwner(), savedUser.getId());
        assertEquals(result.getName(), "Дрель");
    }

    @Test
    void addItemValidEx() {
        ValidationException exception = assertThrows(ValidationException.class, () -> itemService.addItem(1L, itemDtoNotValid));
    }

    @Test
    void addItemIlArEx() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> itemService.addItem(10L, itemDto));
    }

    @Test
    void getById() {
        User savedUser = userRepository.save(user);
        ItemDto savedItem = itemService.addItem(savedUser.getId(), itemDto);
        ItemDto result = itemService.getById(savedUser.getId(), savedItem.getId());
        assertNotNull(result);
        assertEquals(result.getOwner(), savedUser.getId());
        assertEquals(result.getAvailable(), Boolean.TRUE);
    }

    @Test
    void getByIdIlArEx() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> itemService.getById(10L, 1L));
        User savedUser = userRepository.save(user);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> itemService.getById(savedUser.getId(), 10L));
    }

    @Test
    void updateItem() throws ExistException {
        User savedUser = userRepository.save(user);
        ItemDto savedItem = itemService.addItem(savedUser.getId(), itemDto);
        ItemDto newItem = new ItemDto(
                "updatedName",
                "updatedDesc",
                false
        );
        ItemDto result = itemService.updateItem(savedUser.getId(), savedItem.getId(), newItem);
        assertNotNull(result);
        assertEquals(result.getOwner(), savedUser.getId());
        assertEquals(result.getAvailable(), Boolean.FALSE);
        assertEquals(result.getName(), newItem.getName());
    }

    @Test
    void updateItemEx() throws ExistException {
        ExistException exception = assertThrows(ExistException.class, () -> itemService.updateItem(10L, 1L, itemDto));
    }

    @Test
    void getUserItems() {
        User savedUser = userRepository.save(user);
        itemService.addItem(savedUser.getId(), itemDto);
        List<ItemDto> result = itemService.getUserItems(savedUser.getId());
        assertNotNull(result);
        assertEquals(result.size(), 1);
    }

    @Test
    void findItem() {
        User savedUser = userRepository.save(user);
        itemService.addItem(savedUser.getId(), itemDto);
        String text = "дрель";
        List<ItemDto> result = itemService.findItem(text);
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getName(), itemDto.getName());
    }

    @Test
    void findItemEmpt() {
        List<ItemDto> result = itemService.findItem("");
        assertNotNull(result);
        assertEquals(result.size(), 0);
    }

    @Test
    void addComment() {
        User savedUser = userRepository.save(user);
        ItemDto savedItem = itemService.addItem(savedUser.getId(), itemDto);


    }

}
