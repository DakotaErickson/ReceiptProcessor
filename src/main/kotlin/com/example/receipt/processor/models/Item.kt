package com.example.receipt.processor.models

import java.math.BigDecimal

data class Item(
    val shortDescription: String,
    val price: BigDecimal
)
