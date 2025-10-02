package co.com.nauta.rest_api.dto

/**
 * DTO for authentication response
 */
data class AuthResponseDto(
    val token: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long,
    val userId: String,
    val checksum: String
)
