package com.amar.lab.order.web.dto;

import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        String customerName,
        Instant createdAt,
        List<Item> items
) {
    public record Item(Long productId, Integer quantity) {}
}
