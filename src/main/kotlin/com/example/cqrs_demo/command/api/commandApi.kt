package com.example.cqrs_demo.command.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.util.*

data class CreateCart(
    @TargetAggregateIdentifier val cartId: UUID,
)

data class AddItemToCart(
    @TargetAggregateIdentifier val cartId: UUID,
    val itemId: UUID,
    val quantity: Int,
)

data class RemoveItemFromCart(
    @TargetAggregateIdentifier val cartId: UUID,
    val itemId: UUID,
    val quantity: Int,
)

data class PayForCart(
    @TargetAggregateIdentifier val cartId: UUID,
    val amountPaid: Double,
)
