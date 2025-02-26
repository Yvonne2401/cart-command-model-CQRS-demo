package com.example.cqrs_demo.rest

import com.example.cqrs_demo.command.api.AddItemToCart
import com.example.cqrs_demo.command.api.CreateCart
import community.flock.wirespec.generated.java.AddItemToCartEndpoint
import community.flock.wirespec.generated.java.BadRequest
import community.flock.wirespec.generated.java.CartId
import community.flock.wirespec.generated.java.CreateCartEndpoint
import community.flock.wirespec.generated.java.validate
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.util.*

@RestController
class CheckoutController(val commandGateway: CommandGateway) : CreateCartEndpoint.Handler,
    AddItemToCartEndpoint.Handler {
    override suspend fun createCart(request: CreateCartEndpoint.Request): CreateCartEndpoint.Response<*> {
        val cartId = request.body.cartid
        if(!(cartId.validate())){
            return CreateCartEndpoint.Response400(BadRequest("400", "Invalid UUID in path"))
        }
        commandGateway.send<CreateCart>(CreateCart(UUID.fromString(cartId.value)))
        return CreateCartEndpoint.Response200(CartId(cartId.value))
    }

    override suspend fun addItemToCart(request: AddItemToCartEndpoint.Request): AddItemToCartEndpoint.Response<*> {
        if(!CartId(request.path.cartId).validate()){
            return AddItemToCartEndpoint.Response400(BadRequest("400", "Invalid UUID in path"))
        }
        val addItemToCart = request.body
        commandGateway.send<AddItemToCart>(
            AddItemToCart(
                UUID.fromString(request.path.cartId),
                UUID.fromString(addItemToCart.productid.value),
                addItemToCart.quantity.toInt(),
                BigDecimal(addItemToCart.baseprice)
            )
        )
        return AddItemToCartEndpoint.Response200(addItemToCart.cartid)
    }
}