package com.example.cqrs_demo.command

import com.example.cqrs_demo.command.api.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.EventHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.math.BigDecimal
import java.util.*

@Aggregate
class Cart {

    @AggregateIdentifier
    lateinit var cartId: UUID

    val cartEntries: MutableList<CartEntry> = mutableListOf()

    var totalAmount: BigDecimal = BigDecimal.ZERO

    var amountPaid: BigDecimal = BigDecimal.ZERO

    var isOrder: Boolean = false

    constructor()

    @CommandHandler
    constructor(command:CreateCart){
        AggregateLifecycle.apply(CartCreated(command.cartId))
    }

    @CommandHandler
    fun handle(command: AddItemToCart) {
        AggregateLifecycle.apply(ItemAddedToCart(command.cartId, command.productId, command.quantity, command.basePrice))
        if (totalAmount != calculateTotalPrice()) {
            AggregateLifecycle.apply(TotalAmountRecalculated(command.cartId, calculateTotalPrice()))
        }
    }

    @CommandHandler
    fun handle(command: RemoveItemFromCart) {
        AggregateLifecycle.apply(ItemRemovedFromCart(command.cartId, command.productId))
        if (totalAmount != calculateTotalPrice()) {
            AggregateLifecycle.apply(TotalAmountRecalculated(command.cartId, calculateTotalPrice()))
        }
    }

    @CommandHandler
    fun handle(command: AdjustItemQuantityInCart) {
        cartEntries.find { it.productId == command.productId }?.let {
            if (it.quantity - command.quantityDelta <= 0) {
                AggregateLifecycle.apply(ItemRemovedFromCart(command.cartId, command.productId))
            } else {
                AggregateLifecycle.apply(ItemQuantityAdjustedInCart(command.cartId, command.productId, command.quantityDelta))
            }
        }
        if (totalAmount != calculateTotalPrice()) {
            AggregateLifecycle.apply(TotalAmountRecalculated(command.cartId, calculateTotalPrice()))
        }
    }

    @CommandHandler
    fun handle(command: AdjustPriceOfItemInCart) {
        cartEntries.find { it.productId == command.productId }?.let {
            AggregateLifecycle.apply(PriceOfItemInCartAdjusted(command.cartId, command.productId, command.newPrice))
        }
        if (totalAmount != calculateTotalPrice()) {
            AggregateLifecycle.apply(TotalAmountRecalculated(command.cartId, calculateTotalPrice()))
        }
    }

    @CommandHandler
    fun handle(command: PayForCart) {
        apply { PaymentReceived(command.cartId, command.amountPaid) }
        if (amountPaid == totalAmount) {
            apply { OrderCreated(command.cartId, UUID.randomUUID(), cartEntries.map { OrderLine(it.productId, it.quantity, it.price) }, totalAmount) }
        }

    }

    private fun calculateTotalPrice() = cartEntries.sumOf { it.price * it.quantity.toBigDecimal() }

    @EventHandler
    fun on(event: CartCreated) {
        cartId = event.cartId
    }

    @EventHandler
    fun on(event: ItemAddedToCart){
        cartEntries.add(CartEntry(event.cartId, event.quantity, event.basePrice))
    }

    @EventHandler
    fun on(event: ItemQuantityAdjustedInCart){
        cartEntries.find { it.productId == event.productId }?.let {
            it.quantity += event.quantityDelta
        }
    }

    @EventHandler
    fun on(event: PriceOfItemInCartAdjusted){
        cartEntries.find { it.productId == event.productId }?.let {
            it.price = event.newPrice
        }
    }

    @EventHandler
    fun on(event: PaymentReceived){
        amountPaid += event.amountPaid
    }

    @EventHandler
    fun on(event: ItemRemovedFromCart){
        cartEntries.removeIf { it.productId == event.productId }
    }
    @EventHandler
    fun on (event: TotalAmountRecalculated){
        totalAmount = event.totalAmount
    }
    @EventHandler
    fun on (event: OrderCreated){
        isOrder = true
    }
}
