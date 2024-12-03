package com.example.receipt.processor.models

import java.math.BigDecimal

data class Receipt(
    val retailer: String,
    val purchaseDate: String,
    val purchaseTime: String,
    val total: BigDecimal,
    val items: List<Item>
)
