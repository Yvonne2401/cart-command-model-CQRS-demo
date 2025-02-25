package com.example.cqrs_demo.record;

import java.util.UUID;

public record AddItemToCartRecord(
        UUID cartid,
        UUID productid,
        Integer quantity,
        Double basePrice
) {
}
