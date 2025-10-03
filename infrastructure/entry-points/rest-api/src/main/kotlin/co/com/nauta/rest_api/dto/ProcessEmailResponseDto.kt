package co.com.nauta.rest_api.dto

/**
 * DTO for email processing response
 * Represents the result of processing email data
 */
data class ProcessEmailResponseDto(
    val booking: BookingResponseDto? = null,
    val containers: List<ContainerResponseDto> = emptyList(),
    val orders: List<OrderResponseDto> = emptyList(),
    val invoices: List<InvoiceResponseDto> = emptyList(),
    val message: String = "Email processed successfully"
)
