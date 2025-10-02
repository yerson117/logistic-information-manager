package co.com.nauta.rest_api.dto

import java.time.LocalDateTime
import java.util.UUID

/**
 * DTO for order response data
 */
data class OrderResponseDto(
    val orderId: UUID,
    val purchaseCode: String,
    val clientId: UUID,
    val bookingId: UUID? = null,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val invoices: List<InvoiceResponseDto> = emptyList()
)
