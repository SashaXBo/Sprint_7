package ru.practicum.scooter.models;

import lombok.Data;

@Data
public class OrderResponse {
    private boolean success;
    private int track;
}
