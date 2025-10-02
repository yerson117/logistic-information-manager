package co.com.nauta.rest_api.dto

import java.time.LocalDateTime

/**
 * DTO for error responses
 */
data class ErrorResponseDto(
    val timestamp: String = LocalDateTime.now().toString(),
    val status: Int,
    val error: String,
    val message: String,
    val path: String? = null,
    val details: Map<String, Any>? = null
)
