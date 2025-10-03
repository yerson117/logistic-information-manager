package co.com.nauta.rest_api.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.UUID

/**
 * DTO for authentication request
 */
data class AuthRequestDto(
    @field:NotNull(message = "User ID is required")
    val userId: UUID,
    
    @field:NotBlank(message = "Password is required")
    val password: String
)
