package com.example.cqrs_demo.rest

import com.example.cqrs_demo.command.api.AddItemToCart
import com.example.cqrs_demo.command.api.CreateCart
import com.example.cqrs_demo.record.AddItemToCartRecord
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.CompletableFuture

@RestController
class CheckController(
    val commandGateway: CommandGateway
) {

    @PostMapping("/check/carts")
    fun createCart(): CompletableFuture<CreateCart> = commandGateway.send(CreateCart(UUID.randomUUID()))

    @PostMapping("/check/add-item-to-cart")
    fun addItemToCart(@RequestBody(required = true) request: AddItemToCartRecord): CompletableFuture<AddItemToCart> =
        commandGateway.send(
            AddItemToCart(
                request.cartid,
                request.productid,
                request.quantity,
                BigDecimal(request.basePrice)
            )
        )
}