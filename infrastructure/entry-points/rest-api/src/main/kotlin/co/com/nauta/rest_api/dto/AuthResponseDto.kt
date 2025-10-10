package co.com.nauta.rest_api.dto

import java.util.UUID

/**
 * DTO for authentication response
 */
data class AuthResponseDto(
    val token: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long,
    val userId: UUID,
    val checksum: String
)
