package co.com.nauta.rest_api.dto

import java.time.LocalDateTime
import java.util.UUID

/**
 * DTO for invoice response data
 */
data class InvoiceResponseDto(
    val invoiceId: UUID,
    val invoiceCode: String,
    val clientId: UUID,
    val orderId: UUID? = null,
    val amount: String? = null,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
