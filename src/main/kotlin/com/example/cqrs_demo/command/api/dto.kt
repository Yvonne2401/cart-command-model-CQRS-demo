package com.example.cqrs_demo.command.api

import java.util.*

data class AddItemToCartDTO (val cartId: UUID, val productId: UUID, val quantity: Int, val price: Double)