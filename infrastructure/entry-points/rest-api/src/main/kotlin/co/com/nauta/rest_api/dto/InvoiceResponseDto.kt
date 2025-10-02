package co.com.nauta.rest_api.dto

import java.util.UUID

/**
 * DTO for invoice response data
 */
data class InvoiceResponseDto(
    val invoiceId: UUID,
    val invoiceCode: String,
    val clientId: UUID,
    val orderId: UUID? = null,
    val amount: String,
    val createdAt: String,
    val updatedAt: String
)
