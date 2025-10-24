package com.example.demo.cqrs.command

import com.example.demo.cqrs.command.api.AddItemToCartCommand
import com.example.demo.cqrs.command.api.AdjustItemQuantityInCartCommand
import com.example.demo.cqrs.command.api.AdjustPriceOfItemInCartCommand
import com.example.demo.cqrs.command.api.CreateCartCommand
import com.example.demo.cqrs.command.api.RemoveItemFromCartCommand
import com.example.demo.cqrs.events.CartCreated
import com.example.demo.cqrs.events.ItemAddedToCart
import com.example.demo.cqrs.events.ItemQuantityAdjustedInCart
import com.example.demo.cqrs.events.ItemRemovedFromCart
import com.example.demo.cqrs.events.PriceOfItemInCartAdjusted
import com.example.demo.cqrs.events.TotalAmountRecalculated
import org.axonframework.test.aggregate.AggregateTestFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class CartTest {
    lateinit var fixture: AggregateTestFixture<Cart>

    @BeforeEach
    fun setUp() {
        fixture = AggregateTestFixture(Cart::class.java)
    }

    @Test
    fun createCart() {
        fixture.givenNoPriorActivity()
        val cartId = UUID.randomUUID()
        fixture
            .givenNoPriorActivity()
            .`when`(CreateCartCommand(cartId))
            .expectEvents(CartCreated(cartId))
    }

    @Test
    fun addItemToCart() {
        val cartId = UUID.randomUUID()
        val productId = UUID.randomUUID()
        val quantity = 10
        val basePrice = BigDecimal(10.05).setScale(2, RoundingMode.HALF_UP)

        fixture
            .given(CartCreated(cartId))
            .`when`(AddItemToCartCommand(cartId, productId, quantity, basePrice))
            .expectEvents(
                ItemAddedToCart(cartId, productId, quantity, basePrice),
                TotalAmountRecalculated(cartId, basePrice * quantity.toBigDecimal()),
            )
    }

    @Test
    fun adjustQuantityOfItemCart() {
        val cartId = UUID.randomUUID()
        val productId = UUID.randomUUID()
        val quantity = 10
        val newQuantity = 5
        val basePrice = BigDecimal(10.05).setScale(2, RoundingMode.HALF_UP)

        fixture
            .given(
                CartCreated(cartId),
                ItemAddedToCart(cartId, productId, quantity, basePrice),
            ).`when`(AdjustItemQuantityInCartCommand(cartId, productId, newQuantity))
            .expectEvents(
                ItemQuantityAdjustedInCart(cartId, productId, newQuantity),
                TotalAmountRecalculated(cartId, basePrice * newQuantity.toBigDecimal()),
            )
    }

    @Test
    fun adjustPriceOfItemCart() {
        val cartId = UUID.randomUUID()
        val productId = UUID.randomUUID()
        val quantity = 10
        val basePrice = BigDecimal(10.05).setScale(2, RoundingMode.HALF_UP)
        val newPrice = BigDecimal(11.01).setScale(2, RoundingMode.HALF_UP)

        fixture
            .given(
                CartCreated(cartId),
                ItemAddedToCart(cartId, productId, quantity, basePrice),
            ).`when`(AdjustPriceOfItemInCartCommand(cartId, productId, newPrice))
            .expectEvents(
                PriceOfItemInCartAdjusted(cartId, productId, newPrice),
                TotalAmountRecalculated(cartId, newPrice * quantity.toBigDecimal()),
            )
    }

    @Test
    fun removeItemToCart() {
        val cartId = UUID.randomUUID()
        val productId = UUID.randomUUID()
        val quantity = 10
        val basePrice = BigDecimal(10.05).setScale(2, RoundingMode.HALF_UP)

        fixture
            .given(
                CartCreated(cartId),
                ItemAddedToCart(cartId, productId, quantity, basePrice),
                TotalAmountRecalculated(cartId, basePrice * quantity.toBigDecimal()),
            ).`when`(RemoveItemFromCartCommand(cartId, productId))
            .expectEvents(
                ItemRemovedFromCart(cartId, productId),
                TotalAmountRecalculated(cartId, BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)),
            )
    }
}
