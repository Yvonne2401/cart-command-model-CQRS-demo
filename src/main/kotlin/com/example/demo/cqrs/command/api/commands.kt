package com.example.demo.cqrs.command.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.math.BigDecimal
import java.util.*

data class CreateCartCommand(
    @TargetAggregateIdentifier
    val cartId: UUID,
)

data class AddItemToCartCommand(
    @TargetAggregateIdentifier
    val cartId: UUID,
    val productId: UUID,
    val quantity: Int,
    val basePrice: BigDecimal,
)

data class AdjustItemQuantityInCartCommand(
    @TargetAggregateIdentifier
    val cartId: UUID,
    val productId: UUID,
    val quantity: Int,
)

data class AdjustPriceOfItemInCartCommand(
    @TargetAggregateIdentifier
    val cartId: UUID,
    val productId: UUID,
    val price: BigDecimal,
)
data class RemoveItemFromCartCommand(
    @TargetAggregateIdentifier
    val cartId: UUID,
    val productId: UUID,
)

data class ProcessPaymentCommand(
    @TargetAggregateIdentifier
    val cartId: UUID,
    val amountPaid: BigDecimal,
)
