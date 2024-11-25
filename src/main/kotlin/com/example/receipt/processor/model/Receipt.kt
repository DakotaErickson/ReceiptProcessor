package com.example.receipt.processor.model

import java.math.BigDecimal

data class Receipt(
    val retailer: String,
    val purchaseDate: String,
    val purchaseTime: String,
    val total: BigDecimal,
    val items: List<Item>
)
