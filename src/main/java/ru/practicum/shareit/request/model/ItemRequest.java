package ru.practicum.shareit.request.model;

import lombok.Data;

import java.util.Date;

@Data
public class ItemRequest {

    Long id;

    String description;


    Long requestor;

    Date created;
}
