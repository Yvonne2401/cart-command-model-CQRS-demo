package com.example.cqrs_demo.rest

import com.example.cqrs_demo.command.api.AddItemToCart
import com.example.cqrs_demo.command.api.CreateCart
import community.flock.wirespec.generated.java.AddItemToCartEndpoint
import community.flock.wirespec.generated.java.CartId
import community.flock.wirespec.generated.java.CreateCartEndpoint
import community.flock.wirespec.generated.java.Error
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import java.math.BigDecimal
import java.util.*

@RestController
class CheckoutController (val commandGateway: CommandGateway, val wirespecWebClient: WebClient
) : CreateCartEndpoint.Handler, AddItemToCartEndpoint.Handler
{
    override suspend fun createCart(request: CreateCartEndpoint.Request): CreateCartEndpoint.Response<*> {
        val cartId = UUID.randomUUID()
        commandGateway.send<CreateCart>(CreateCart(cartId))
        return CreateCartEndpoint.Response200(CartId(cartId.toString()))
    }

    override suspend fun addItemToCart(request: AddItemToCartEndpoint.Request): AddItemToCartEndpoint.Response<*> {
        println("Incoming request body : ${request?.body}")
        println("request?.body?.cartid?.value() : ${request?.body?.cartid?.value}")

        request?.body?.let { addItemToCartRequest ->
            commandGateway.send<AddItemToCart>(
                AddItemToCart(
                    UUID.fromString(addItemToCartRequest.cartid.value),
                    UUID.fromString(addItemToCartRequest.productid.value),
                    addItemToCartRequest.quantity.toInt(),
                    BigDecimal(addItemToCartRequest.baseprice)
                ))
                return AddItemToCartEndpoint.Response200(addItemToCartRequest.cartid)
        }
        return AddItemToCartEndpoint.Response404(Error("404", "Item not found"))
    }
}