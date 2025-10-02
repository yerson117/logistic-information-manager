package co.com.nauta.rest_api.dto

import java.util.UUID

/**
 * DTO for booking response data
 */
data class BookingResponseDto(
    val bookingId: UUID,
    val bookingCode: String,
    val clientId: UUID,
    val createdAt: String,
    val updatedAt: String
)
