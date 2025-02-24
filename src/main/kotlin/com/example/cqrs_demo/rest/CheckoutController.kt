package com.example.cqrs_demo.rest

import com.example.cqrs_demo.command.api.AddItemToCart
import com.example.cqrs_demo.command.api.CreateCart
import community.flock.wirespec.generated.java.AddItemToCartEndpoint
import community.flock.wirespec.generated.java.CartId
import community.flock.wirespec.generated.java.CreateCartEndpoint
import community.flock.wirespec.generated.java.Error
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.CompletableFuture

@RestController
class CheckoutController (val commandGateway: CommandGateway) : CreateCartEndpoint.Handler, AddItemToCartEndpoint.Handler
{
    override fun createCart(request: CreateCartEndpoint.Request?): CompletableFuture<CreateCartEndpoint.Response<*>> {
        val cartId = UUID.randomUUID()
        commandGateway.send<CreateCart>(CreateCart(cartId))
        return CompletableFuture.completedFuture(CreateCartEndpoint.Response200(CartId(cartId.toString())))
    }

    override fun addItemToCart(request: AddItemToCartEndpoint.Request?): CompletableFuture<AddItemToCartEndpoint.Response<*>> {
        request?.body?.let { addItemToCartRequest ->
            commandGateway.send<AddItemToCart>(
                AddItemToCart(
                    UUID.fromString(addItemToCartRequest.cartid.value()),
                    UUID.fromString(addItemToCartRequest.productid.value()),
                    addItemToCartRequest.quantity.toInt(),
                    BigDecimal(addItemToCartRequest.baseprice)
                ))
                return CompletableFuture.completedFuture(AddItemToCartEndpoint.Response200(addItemToCartRequest.cartid))
        }
        return CompletableFuture.completedFuture(AddItemToCartEndpoint.Response404(community.flock.wirespec.generated.java.Error("404", Optional.of("Item not found"))))
    }
}