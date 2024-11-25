package com.example.receipt.processor.model

import java.math.BigDecimal

data class Item(
    val shortDescription: String,
    val price: BigDecimal
)
