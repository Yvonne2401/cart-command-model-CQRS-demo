package com.example.cqrs_demo.command.api

import java.math.BigDecimal
import java.util.*

data class CartCreated(
    val cartId: UUID,
)

data class ItemAddedToCart(
    val cartId: UUID,
    val productId: UUID,
    val quantity: Int,
    val basePrice: BigDecimal,
)

data class ItemRemovedFromCart(
    val cartId: UUID,
    val productId: UUID,
)

data class TotalAmountRecalculated(
    val cartId: UUID,
    val totalAmount: BigDecimal,
)

data class ItemQuantityAdjustedInCart(
    val cartId: UUID,
    val productId: UUID,
    val quantityDelta: Int,
)

data class PriceOfItemInCartAdjusted(
    val cartId: UUID,
    val productId: UUID,
    val newPrice: BigDecimal,
)

data class OrderCreated(
    val cartId: UUID,
    val orderId: UUID,
    val orderLines: List<OrderLine>,
    val totalAmount: BigDecimal,
)

data class OrderLine(
    val productId: UUID,
    val quantity: Int,
    val unitPrice: BigDecimal,
)

data class PaymentReceived(
    val cartId: UUID,
    val amountPaid: BigDecimal,
)

