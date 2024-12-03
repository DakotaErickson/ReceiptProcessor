package com.example.receipt.processor.service

import com.example.receipt.processor.datasource.ReceiptDataSource
import com.example.receipt.processor.models.Receipt
import com.example.receipt.processor.models.responses.PointsResponse
import com.example.receipt.processor.models.responses.ProcessResponse
import org.springframework.stereotype.Service
import java.util.*

@Service
class ReceiptService(
    private val dataSource: ReceiptDataSource
) {
    fun getPoints(id: UUID) : PointsResponse = dataSource.getPoints(id)

    fun processReceipt(receipt: Receipt) : ProcessResponse = dataSource.processReceipt(receipt)
}