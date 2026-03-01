package com.amar.lab.product.web.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String sku,
        BigDecimal price
) {}
