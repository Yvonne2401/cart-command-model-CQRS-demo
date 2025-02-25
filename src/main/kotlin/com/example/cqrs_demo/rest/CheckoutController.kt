package com.example.cqrs_demo.rest

import com.example.cqrs_demo.command.api.AddItemToCart
import com.example.cqrs_demo.command.api.CreateCart
import community.flock.wirespec.generated.java.AddItemToCartEndpoint
import community.flock.wirespec.generated.java.CartId
import community.flock.wirespec.generated.java.CreateCartEndpoint
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.util.*

@RestController
class CheckoutController(val commandGateway: CommandGateway) : CreateCartEndpoint.Handler,
    AddItemToCartEndpoint.Handler {
    override suspend fun createCart(request: CreateCartEndpoint.Request): CreateCartEndpoint.Response<*> {
        val cartId = request.body.cartid.value
        println("Incoming create cart request with id: : $cartId")
        commandGateway.send<CreateCart>(CreateCart(UUID.fromString(cartId)))
        return CreateCartEndpoint.Response200(CartId(cartId))
    }

    override suspend fun addItemToCart(request: AddItemToCartEndpoint.Request): AddItemToCartEndpoint.Response<*> {
        val addItemToCart = request.body
        println("Incoming request body : $addItemToCart")

        commandGateway.send<AddItemToCart>(
            AddItemToCart(
                UUID.fromString(addItemToCart.cartid.value),
                UUID.fromString(addItemToCart.productid.value),
                addItemToCart.quantity.toInt(),
                BigDecimal(addItemToCart.baseprice)
            )
        )
        return AddItemToCartEndpoint.Response200(addItemToCart.cartid)
    }
}