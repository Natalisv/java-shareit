package ru.practicum.shareit.request.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;


import java.util.List;
import java.util.stream.Collectors;

@Service
public final class ItemRequestMapper {

    private ItemRequestMapper() {
    }

    @Autowired
    private ItemRepository itemRepository;

    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return new ItemRequest(
                itemRequestDto.getDescription()
        );
    }

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        List<Item> items = itemRepository.findByRequestId(itemRequest.getRequestorId());
        List<ItemDtoShort> itemsDtoShort = items.stream().map(ItemMapper::toItemDtoShort).collect(Collectors.toList());
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestorId(),
                itemRequest.getCreated(),
                itemsDtoShort
        );
    }
}
