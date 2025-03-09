package com.example.demo.cqrs.command.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.math.BigDecimal
import java.util.*

data class CreateCart(
    @TargetAggregateIdentifier
    val cartId: UUID,
)

data class AddItemToCart(
    @TargetAggregateIdentifier
    val cartId: UUID,
    val productId: UUID,
    val quantity: Int,
    val basePrice: BigDecimal,
)

data class AdjustItemQuantityInCart(
    @TargetAggregateIdentifier
    val cartId: UUID,
    val productId: UUID,
    val quantity: Int,
)

data class AdjustPriceOfItemInCart(
    @TargetAggregateIdentifier
    val cartId: UUID,
    val productId: UUID,
    val price: BigDecimal,
)
data class RemoveItemFromCart(
    @TargetAggregateIdentifier
    val cartId: UUID,
    val productId: UUID,
)

data class ProcessPayment(
    @TargetAggregateIdentifier
    val cartId: UUID,
    val amountPaid: BigDecimal,
)
