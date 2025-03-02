package com.example.cqrs_demo.command

import com.example.cqrs_demo.command.api.AddItemToCart
import com.example.cqrs_demo.command.api.AdjustItemQuantityInCart
import com.example.cqrs_demo.command.api.AdjustPriceOfItemInCart
import com.example.cqrs_demo.command.api.CartCreated
import com.example.cqrs_demo.command.api.CreateCart
import com.example.cqrs_demo.command.api.ItemAddedToCart
import com.example.cqrs_demo.command.api.ItemQuantityAdjustedInCart
import com.example.cqrs_demo.command.api.ItemRemovedFromCart
import com.example.cqrs_demo.command.api.OrderCreated
import com.example.cqrs_demo.command.api.OrderLine
import com.example.cqrs_demo.command.api.PayForCart
import com.example.cqrs_demo.command.api.PaymentReceived
import com.example.cqrs_demo.command.api.PriceOfItemInCartAdjusted
import com.example.cqrs_demo.command.api.RemoveItemFromCart
import com.example.cqrs_demo.command.api.TotalAmountRecalculated
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.EventHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

@Aggregate
class Cart {

    @AggregateIdentifier
    lateinit var cartId: UUID

    var cartEntries: MutableMap<UUID, CartEntry> = mutableMapOf()

    var totalAmount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)

    var amountPaid = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)

    var isOrder: Boolean = false

    constructor()

    @CommandHandler
    constructor(command: CreateCart) {
        AggregateLifecycle.apply(CartCreated(command.cartId))
    }

    @CommandHandler
    fun handle(command: AddItemToCart) {
        if (!cartEntries.containsKey(command.productId)) {
            AggregateLifecycle.apply(
                ItemAddedToCart(
                    command.cartId,
                    command.productId,
                    command.quantity,
                    command.basePrice
                )
            )
            if (totalAmount != calculateTotalPrice()) {
                AggregateLifecycle.apply(TotalAmountRecalculated(command.cartId, calculateTotalPrice()))
            }
        }
    }

    @CommandHandler
    fun handle(command: RemoveItemFromCart) {
        if (cartEntries.containsKey(command.productId)) {
            AggregateLifecycle.apply(ItemRemovedFromCart(command.cartId, command.productId))
            if (totalAmount != calculateTotalPrice()) {
                AggregateLifecycle.apply(TotalAmountRecalculated(command.cartId, calculateTotalPrice()))
            }
            }
        }

    @CommandHandler
    fun handle(command: AdjustItemQuantityInCart) {
        cartEntries[command.productId]?.let {
            if (command.quantity <= 0) {
                AggregateLifecycle.apply(ItemRemovedFromCart(command.cartId, command.productId))
            } else {
                AggregateLifecycle.apply(
                    ItemQuantityAdjustedInCart(
                        command.cartId,
                        command.productId,
                        command.quantity
                    )
                )
            }
            if (totalAmount != calculateTotalPrice()) {
                AggregateLifecycle.apply(TotalAmountRecalculated(command.cartId, calculateTotalPrice()))
            }
        }
    }

    @CommandHandler
    fun handle(command: AdjustPriceOfItemInCart) {
        cartEntries[command.productId]?.let {
            AggregateLifecycle.apply(PriceOfItemInCartAdjusted(command.cartId, command.productId, command.price))
            if (totalAmount != calculateTotalPrice()) {
                AggregateLifecycle.apply(TotalAmountRecalculated(command.cartId, calculateTotalPrice()))
            }

        }
    }

    @CommandHandler
    fun handle(command: PayForCart) {
        if (totalAmount > amountPaid) {
            if (amountPaid + command.amountPaid <= totalAmount) {
                AggregateLifecycle.apply(PaymentReceived(command.cartId, command.amountPaid))
                if (amountPaid == totalAmount) {
                    AggregateLifecycle.apply(
                        OrderCreated(
                            command.cartId,
                            UUID.randomUUID(),
                            cartEntries.values.map { OrderLine(it.productId, it.quantity, it.price) },
                            totalAmount,
                            amountPaid
                        )
                    )
                }
            }
        }

    }

    private fun calculateTotalPrice () : BigDecimal {
        if (cartEntries.isEmpty()) {return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)}
        return cartEntries.values.sumOf { it.price * it.quantity.toBigDecimal() }.setScale(2, RoundingMode.HALF_UP)
    }

    @EventHandler
    fun on(event: CartCreated) {
        cartId = event.cartId
    }

    @EventHandler
    fun on(event: ItemAddedToCart) {
        cartEntries[event.productId] = CartEntry(event.productId, event.quantity, event.basePrice)
    }

    @EventHandler
    fun on(event: ItemQuantityAdjustedInCart) {
        cartEntries[event.productId]?.let {
            it.quantity = event.quantity
        }
    }

    @EventHandler
    fun on(event: PriceOfItemInCartAdjusted) {
        cartEntries[event.productId]?.let {
            it.price = event.price
        }
    }

    @EventHandler
    fun on(event: PaymentReceived) {
        amountPaid += event.amountPaid
    }

    @EventHandler
    fun on(event: ItemRemovedFromCart) {
        cartEntries.remove(event.productId)
    }

    @EventHandler
    fun on(event: TotalAmountRecalculated) {
        totalAmount = event.totalAmount
    }

    @EventHandler
    fun on(event: OrderCreated) {
        isOrder = true
    }
}
