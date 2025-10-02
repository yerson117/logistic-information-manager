package co.com.nauta.rest_api.dto

import java.util.UUID

/**
 * DTO for order response data
 */
data class OrderResponseDto(
    val orderId: UUID,
    val purchaseCode: String,
    val clientId: UUID,
    val bookingId: UUID? = null,
    val createdAt: String,
    val updatedAt: String,
    val invoices: List<InvoiceResponseDto> = emptyList()
)
