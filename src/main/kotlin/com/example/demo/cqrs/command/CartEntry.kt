package com.example.demo.cqrs.command

import java.math.BigDecimal
import java.util.*

data class CartEntry(
    var productId: UUID,
    var quantity: Int,
    var price: BigDecimal
)
