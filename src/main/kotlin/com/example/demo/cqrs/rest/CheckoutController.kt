package com.example.demo.cqrs.rest

import com.example.demo.cqrs.command.api.AddItemToCartCommand
import com.example.demo.cqrs.command.api.AdjustItemQuantityInCartCommand
import com.example.demo.cqrs.command.api.AdjustPriceOfItemInCartCommand
import com.example.demo.cqrs.command.api.CreateCartCommand
import com.example.demo.cqrs.command.api.RemoveItemFromCartCommand
import community.flock.wirespec.generated.endpoint.AddItemToCart
import community.flock.wirespec.generated.endpoint.AdjustItemQuantityInCart
import community.flock.wirespec.generated.endpoint.AdjustPriceOfItemInCart
import community.flock.wirespec.generated.endpoint.CreateCart
import community.flock.wirespec.generated.endpoint.RemoveItemFromCart
import community.flock.wirespec.generated.model.BadRequest
import community.flock.wirespec.generated.model.validate
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

import java.math.RoundingMode
import java.util.*

private const val INVALID_UUID_IN_PATH_ERROR = "Invalid UUID in path"

@RestController
class CheckoutController(val commandGateway: CommandGateway) : CreateCart.Handler,
    AddItemToCart.Handler, AdjustItemQuantityInCart.Handler, AdjustPriceOfItemInCart.Handler,
    RemoveItemFromCart.Handler{
    override suspend fun createCart(request: CreateCart.Request): CreateCart.Response<*> {
        val cartId = request.body.cartId
        if (!(cartId.validate())) {
            return CreateCart.Response400(BadRequest("400", INVALID_UUID_IN_PATH_ERROR))
        }
        commandGateway.send<CreateCartCommand>(CreateCartCommand(UUID.fromString(cartId.value)))
        return CreateCart.Response200(community.flock.wirespec.generated.model.CartId(cartId.value))
    }

    override suspend fun addItemToCart(request: AddItemToCart.Request): AddItemToCart.Response<*> {
        val cartIdString = request.path.cartId
        val cartId = community.flock.wirespec.generated.model.CartId(cartIdString)
        if (!cartId.validate()) {
            return AddItemToCart.Response400(BadRequest("400", INVALID_UUID_IN_PATH_ERROR))
        }
        val addItemToCart = request.body
        commandGateway.send<AddItemToCart>(
            AddItemToCartCommand(
                UUID.fromString(cartIdString),
                UUID.fromString(addItemToCart.productId.value),
                addItemToCart.quantity.toInt(),
                BigDecimal(addItemToCart.price).setScale(2, RoundingMode.HALF_UP)
            )
        )
        return AddItemToCart.Response200(cartId)
    }

    override suspend fun adjustItemQuantityInCart(request: AdjustItemQuantityInCart.Request): AdjustItemQuantityInCart.Response<*> {
        val cartId = community.flock.wirespec.generated.model.CartId(request.path.cartId)
        if (!cartId.validate()) {
            return AdjustItemQuantityInCart.Response400(BadRequest("400", INVALID_UUID_IN_PATH_ERROR))
        }
        val adjustItemQuantityInCart = request.body
        commandGateway.send<AdjustItemQuantityInCart>(
            AdjustItemQuantityInCartCommand(
                UUID.fromString(cartId.value),
                UUID.fromString(request.path.productId),
                adjustItemQuantityInCart.quantity.toInt()
            )
        )
        return AdjustItemQuantityInCart.Response200(cartId)
    }

    override suspend fun adjustPriceOfItemInCart(request: AdjustPriceOfItemInCart.Request): AdjustPriceOfItemInCart.Response<*> {
        val cartId = community.flock.wirespec.generated.model.CartId(request.path.cartId)
        if (!cartId.validate()) {
            return AdjustPriceOfItemInCart.Response400(BadRequest("400", INVALID_UUID_IN_PATH_ERROR))
        }
        val adjustPriceOfItemInCart = request.body
        commandGateway.send<AdjustPriceOfItemInCart>(
            AdjustPriceOfItemInCartCommand(
                UUID.fromString(request.path.cartId),
                UUID.fromString(request.path.productId),
                BigDecimal(adjustPriceOfItemInCart.price).setScale(2, RoundingMode.HALF_UP)
            )
        )
        return AdjustPriceOfItemInCart.Response200(cartId)
    }

    override suspend fun removeItemFromCart(request: RemoveItemFromCart.Request): RemoveItemFromCart.Response<*> {
        val cartId = community.flock.wirespec.generated.model.CartId(request.path.cartId)
        if (!cartId.validate()) {
            return RemoveItemFromCart.Response400(BadRequest("400", INVALID_UUID_IN_PATH_ERROR))
        }
        commandGateway.send<RemoveItemFromCart>(
            RemoveItemFromCartCommand(
                UUID.fromString(request.path.cartId),
                UUID.fromString(request.path.productId),
            )
        )
        return RemoveItemFromCart.Response200(cartId)
    }

    }