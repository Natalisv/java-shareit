package ru.practicum.shareit.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private ItemRequestMapper itemRequestMapper;

    public ItemRequestDto addRequest(Long userId, ItemRequestDto itemRequestDto) {
        if (Boolean.TRUE.equals(isValid(itemRequestDto))) {
            if (userId != null && isContainsUser(userId)) {
                ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
                itemRequest.setRequestorId(userId);
                itemRequest.setCreated(LocalDateTime.now());
                return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
            } else {
                log.error("Пользователя с userId = " + userId + " не существует");
                throw new IllegalArgumentException();
            }
        } else {
            log.error("Невалидная сущность");
            throw new ValidationException("Невалидная сущность");
        }
    }

    public List<ItemRequestDto> getRequests(Long userId) {
        if (userId != null && isContainsUser(userId)) {
            List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorId(userId);
            List<ItemRequestDto> itemRequestsDto = itemRequests.stream().map(i -> itemRequestMapper.toItemRequestDto(i))
                    .collect(Collectors.toList());
            return itemRequestsDto.stream().sorted(Comparator.comparing(ItemRequestDto::getCreated)).collect(Collectors.toList());
        } else {
            log.error("Пользователя с userId = " + userId + " не существует");
            throw new IllegalArgumentException();
        }
    }

    public ItemRequestDto getRequestById(Long userId, Long requestId) {
        if (requestId != null && isContainsUser(userId)) {
            return itemRequestMapper.toItemRequestDto(itemRequestRepository.findById(requestId).orElseThrow(() -> {
                        throw new IllegalArgumentException();
                    }
            ));
        } else {
            log.error("Пользователя с userId = " + requestId + " не существует");
            throw new IllegalArgumentException();
        }
    }

    public List<ItemRequestDto> getOtherRequests(Long userId, Integer from, Integer size) {
        if (userId != null && isContainsUser(userId)) {
            if (from == null || size == null) {
                return Collections.emptyList();
            }
            if (from == 0 && size == 0 || from < 0 || size < 0) {
                throw new ValidationException();
            }
            List<ItemRequest> itemRequests = itemRequestRepository.findAll();
            itemRequests = itemRequests.stream().filter(i -> !i.getRequestorId().equals(userId)).collect(Collectors.toList());
            if (itemRequests != null) {
                itemRequests = itemRequests.stream().sorted(Comparator.comparing(ItemRequest::getCreated)).skip(from)
                        .limit(size).collect(Collectors.toList());
                return itemRequests.stream().map(i -> itemRequestMapper.toItemRequestDto(i)).collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        } else {
            log.error("Пользователя с userId = " + userId + " не существует");
            throw new IllegalArgumentException();
        }
    }


    private boolean isContainsUser(Long id) {
        return userRepository.existsById(id);
    }

    private boolean isValid(ItemRequestDto itemRequest) {
        return itemRequest.getDescription() != null && !itemRequest.getDescription().isEmpty();
    }
}
