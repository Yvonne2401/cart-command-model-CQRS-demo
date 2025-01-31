package com.example.cqrs_demo.command.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.math.BigDecimal
import java.util.*

data class CreateCart(
    @TargetAggregateIdentifier val cartId: UUID,
)

data class AddItemToCart(
    @TargetAggregateIdentifier val cartId: UUID,
    val productId: UUID,
    val quantity: Int,
    val basePrice: BigDecimal,
)

data class AdjustItemQuantityInCart(
    @TargetAggregateIdentifier val cartId: UUID,
    val productId: UUID,
    val quantityDelta: Int,
)

data class AdjustPriceOfItemInCart(
    @TargetAggregateIdentifier val cartId: UUID,
    val productId: UUID,
    val newPrice: BigDecimal,
)
data class RemoveItemFromCart(
    @TargetAggregateIdentifier val cartId: UUID,
    val productId: UUID,
)

data class PayForCart(
    @TargetAggregateIdentifier val cartId: UUID,
    val amountPaid: BigDecimal,
)
