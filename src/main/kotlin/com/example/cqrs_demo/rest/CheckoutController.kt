package com.example.cqrs_demo.rest

import com.example.cqrs_demo.command.api.AddItemToCart
import com.example.cqrs_demo.command.api.AdjustItemQuantityInCart
import com.example.cqrs_demo.command.api.AdjustPriceOfItemInCart
import com.example.cqrs_demo.command.api.CreateCart
import com.example.cqrs_demo.command.api.PayForCart
import com.example.cqrs_demo.command.api.RemoveItemFromCart
import community.flock.wirespec.generated.java.AddItemToCartEndpoint
import community.flock.wirespec.generated.java.AdjustItemQuantityInCartEndpoint
import community.flock.wirespec.generated.java.AdjustPriceOfItemInCartEndpoint
import community.flock.wirespec.generated.java.BadRequest
import community.flock.wirespec.generated.java.CartId
import community.flock.wirespec.generated.java.CreateCartEndpoint
import community.flock.wirespec.generated.java.PayForCartEndpoint
import community.flock.wirespec.generated.java.RemoveItemFromCartEndpoint
import community.flock.wirespec.generated.java.validate
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

import java.math.RoundingMode
import java.util.*

private const val INVALID_UUID_IN_PATH_ERROR = "Invalid UUID in path"

@RestController
class CheckoutController(val commandGateway: CommandGateway) : CreateCartEndpoint.Handler,
    AddItemToCartEndpoint.Handler, AdjustItemQuantityInCartEndpoint.Handler, AdjustPriceOfItemInCartEndpoint.Handler,
    RemoveItemFromCartEndpoint.Handler, PayForCartEndpoint.Handler {
    override suspend fun createCart(request: CreateCartEndpoint.Request): CreateCartEndpoint.Response<*> {
        val cartId = request.body.cartId
        if (!(cartId.validate())) {
            return CreateCartEndpoint.Response400(BadRequest("400", INVALID_UUID_IN_PATH_ERROR))
        }
        commandGateway.send<CreateCart>(CreateCart(UUID.fromString(cartId.value)))
        return CreateCartEndpoint.Response200(CartId(cartId.value))
    }

    override suspend fun addItemToCart(request: AddItemToCartEndpoint.Request): AddItemToCartEndpoint.Response<*> {
        val cartIdString = request.path.cartId
        val cartId = CartId(cartIdString)
        if (!cartId.validate()) {
            return AddItemToCartEndpoint.Response400(BadRequest("400", INVALID_UUID_IN_PATH_ERROR))
        }
        val addItemToCart = request.body
        commandGateway.send<AddItemToCart>(
            AddItemToCart(
                UUID.fromString(cartIdString),
                UUID.fromString(addItemToCart.productId.value),
                addItemToCart.quantity.toInt(),
                BigDecimal(addItemToCart.price).setScale(2, RoundingMode.HALF_UP)
            )
        )
        return AddItemToCartEndpoint.Response200(cartId)
    }

    override suspend fun adjustItemQuantityInCart(request: AdjustItemQuantityInCartEndpoint.Request): AdjustItemQuantityInCartEndpoint.Response<*> {
        val cartId = CartId(request.path.cartId)
        if (!cartId.validate()) {
            return AdjustItemQuantityInCartEndpoint.Response400(BadRequest("400", INVALID_UUID_IN_PATH_ERROR))
        }
        val adjustItemQuantityInCart = request.body
        commandGateway.send<AdjustItemQuantityInCart>(
            AdjustItemQuantityInCart(
                UUID.fromString(cartId.value),
                UUID.fromString(request.path.productId),
                adjustItemQuantityInCart.quantity.toInt()
            )
        )
        return AdjustItemQuantityInCartEndpoint.Response200(cartId)
    }

    override suspend fun adjustPriceOfItemInCart(request: AdjustPriceOfItemInCartEndpoint.Request): AdjustPriceOfItemInCartEndpoint.Response<*> {
        val cartId = CartId(request.path.cartId)
        if (!cartId.validate()) {
            return AdjustPriceOfItemInCartEndpoint.Response400(BadRequest("400", INVALID_UUID_IN_PATH_ERROR))
        }
        val adjustPriceOfItemInCart = request.body
        commandGateway.send<AdjustPriceOfItemInCart>(
            AdjustPriceOfItemInCart(
                UUID.fromString(request.path.cartId),
                UUID.fromString(request.path.productId),
                BigDecimal(adjustPriceOfItemInCart.price).setScale(2)
            )
        )
        return AdjustPriceOfItemInCartEndpoint.Response200(cartId)
    }

    override suspend fun removeItemFromCart(request: RemoveItemFromCartEndpoint.Request): RemoveItemFromCartEndpoint.Response<*> {
        val cartId = CartId(request.path.cartId)
        if (!cartId.validate()) {
            return RemoveItemFromCartEndpoint.Response400(BadRequest("400", INVALID_UUID_IN_PATH_ERROR))
        }
        commandGateway.send<RemoveItemFromCartEndpoint>(
            RemoveItemFromCart(
                UUID.fromString(request.path.cartId),
                UUID.fromString(request.path.productId),
            )
        )
        return RemoveItemFromCartEndpoint.Response200(cartId)
    }

    override suspend fun payForCart(request: PayForCartEndpoint.Request): PayForCartEndpoint.Response<*> {
        val cartId = CartId(request.path.cartId)
        if (!cartId.validate()) {
            return PayForCartEndpoint.Response400(BadRequest("400", INVALID_UUID_IN_PATH_ERROR))
        }
        val payForCart = request.body
        commandGateway.send<PayForCart>(
            PayForCart(
                UUID.fromString(request.path.cartId),
                BigDecimal(payForCart.amountPaid).setScale(2, RoundingMode.HALF_UP)
            )
        )
        return PayForCartEndpoint.Response200(cartId)
    }
}