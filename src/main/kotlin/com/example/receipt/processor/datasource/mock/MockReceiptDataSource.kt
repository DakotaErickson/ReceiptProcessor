package com.example.receipt.processor.datasource.mock

import com.example.receipt.processor.datasource.ReceiptDataSource
import com.example.receipt.processor.models.Item
import com.example.receipt.processor.models.Receipt
import com.example.receipt.processor.models.responses.PointsResponse
import com.example.receipt.processor.models.responses.ProcessResponse
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.NoSuchElementException
import kotlin.math.ceil
import kotlin.math.floor

@Repository
class MockReceiptDataSource : ReceiptDataSource {

    val idToPoints = mutableMapOf<UUID, BigDecimal>()

    override fun getPoints(id: UUID): PointsResponse = idToPoints[id]?.let { PointsResponse(points = it) } ?:
        throw NoSuchElementException("No receipt found with ID: $id")

    override fun processReceipt(receipt: Receipt): ProcessResponse {
        """            
                One point for every alphanumeric character in the retailer name.
                50 points if the total is a round dollar amount with no cents.
                25 points if the total is a multiple of 0.25.
                5 points for every two items on the receipt.
                If the trimmed length of the item description is a multiple of 3, multiply the price by 0.2 and 
                round up to the nearest integer. The result is the number of points earned.
                6 points if the day in the purchase date is odd.
                10 points if the time of purchase is after 2:00pm and before 4:00pm.
        """.trimIndent()

        var points = BigDecimal.ZERO
        points += retailerNamePoints(receipt.retailer)
        points += totalHasNoCentsPoints(receipt.total)
        points += totalIsMultipleOfAQuarterPoints(receipt.total)
        points += numberOfItemsPoints(receipt.items)
        points += trimmedLengthOfItemsDescriptionPoints(receipt.items)
        points += dayOfPurchaseIsOddPoints(receipt.purchaseDate)
        points += purchaseTimeWithinWindowPoints(receipt.purchaseTime)

        val receiptId = UUID.randomUUID()

        idToPoints[receiptId] = points

        return ProcessResponse(id = receiptId)
    }

    fun retailerNamePoints(name: String): BigDecimal {
        val eachCharacterIsWorth = BigDecimal(1)

        return name.count { it.isLetterOrDigit() }
            .toBigDecimal() * eachCharacterIsWorth
    }

    fun totalHasNoCentsPoints(total: BigDecimal): BigDecimal {
        return if (total.stripTrailingZeros().scale() == 0) {
            BigDecimal(50)
        } else {
            BigDecimal.ZERO
        }
    }

    fun totalIsMultipleOfAQuarterPoints(total: BigDecimal): BigDecimal {
        val multipleOfQuarter = setOf(0.00, 0.25, 0.50, 0.75)
        val cents = total.toString().split(".").last().toDouble()
        return if (cents in multipleOfQuarter) {
            BigDecimal(25)
        } else {
            BigDecimal.ZERO
        }
    }

    fun numberOfItemsPoints(items: List<Item>): BigDecimal = BigDecimal(5) * BigDecimal(items.size / 2)

    fun trimmedLengthOfItemsDescriptionPoints(items: List<Item>): BigDecimal {
        return items.filter { it.shortDescription.trim().length % 3 == 0 }
            .sumOf { ceil(0.2 * it.price.toDouble()) }
            .toBigDecimal()
    }

    fun dayOfPurchaseIsOddPoints(purchaseDate: String): BigDecimal {
        val day = purchaseDate.split("-").last().toInt()
        return when (day % 2) {
            1 -> BigDecimal(6)
            else -> BigDecimal.ZERO
        }
    }

    fun purchaseTimeWithinWindowPoints(timeString: String): BigDecimal {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val time = LocalTime.parse(timeString, formatter)

        return if (time in LocalTime.of(14, 0) .. LocalTime.of(16, 0)) {
            BigDecimal(10)
        } else {
            BigDecimal.ZERO
        }
    }

}