package com.example.receipt.processor.controller

import com.example.receipt.processor.models.responses.PointsResponse
import com.example.receipt.processor.models.responses.ProcessResponse
import com.example.receipt.processor.models.Receipt
import com.example.receipt.processor.service.ReceiptService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/receipts")
class ReceiptController(
    private val service: ReceiptService
) {
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @GetMapping("/{id}/points")
    fun getPoints(@PathVariable id: UUID): PointsResponse = service.getPoints(id)

    @PostMapping("/process")
    fun processReceipt(@RequestBody receipt: Receipt): ProcessResponse = service.processReceipt(receipt)
}