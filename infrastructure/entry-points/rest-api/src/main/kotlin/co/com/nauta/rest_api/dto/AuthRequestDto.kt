package co.com.nauta.rest_api.dto

import jakarta.validation.constraints.NotBlank

/**
 * DTO for authentication request
 */
data class AuthRequestDto(
    @field:NotBlank(message = "User ID is required")
    val userId: String,
    
    @field:NotBlank(message = "Password is required")
    val password: String
)
