package com.example.demo.cqrs.events

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
    val quantity: Int,
)

data class PriceOfItemInCartAdjusted(
    val cartId: UUID,
    val productId: UUID,
    val price: BigDecimal,
)

data class OrderCreated(
    val cartId: UUID,
    val orderId: UUID,
    val orderLines: List<OrderLine>,
    val totalAmount: BigDecimal,
    val amountPaid: BigDecimal
)

data class OrderLine(
    val productId: UUID,
    val quantity: Int,
    val price: BigDecimal,
)

data class PaymentReceived(
    val cartId: UUID,
    val amountPaid: BigDecimal,
)

