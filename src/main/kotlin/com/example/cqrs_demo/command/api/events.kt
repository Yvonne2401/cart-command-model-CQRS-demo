package com.example.cqrs_demo.command.api

import java.util.*

data class CartCreated(
    val cartId: UUID,
)

data class ItemAddedToCart(
    val cartId: UUID,
    val itemId: UUID,
    val quantity: Int,
)

data class ItemRemovedFromCart(
    val cartId: UUID,
    val itemId: UUID,
    val quantity: Int,
)

data class OrderCreated(
    val cartId: UUID,
)

data class OrderFulfilled(
    val cartId: UUID,
    val amountPaid: Double,
)
