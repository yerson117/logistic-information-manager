package co.com.nauta.rest_api.dto

import java.util.UUID

/**
 * DTO for container response data
 */
data class ContainerResponseDto(
    val containerId: UUID,
    val containerCode: String,
    val clientId: UUID,
    val bookingId: UUID? = null,
    val createdAt: String,
    val updatedAt: String
)
