package com.example.receipt.processor.datasource

import com.example.receipt.processor.models.Receipt
import com.example.receipt.processor.models.responses.PointsResponse
import com.example.receipt.processor.models.responses.ProcessResponse
import java.util.*

interface ReceiptDataSource {
    fun getPoints(id: UUID): PointsResponse
    fun processReceipt(receipt: Receipt): ProcessResponse
}