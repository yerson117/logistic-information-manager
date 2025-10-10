package co.com.nauta.rest_api.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@Schema(description = "Request DTO for user authentication")
data class AuthRequestDto(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email must be valid")
    @Schema(description = "User email", example = "user@example.com")
    val email: String,
    
    @field:NotBlank(message = "Password is required")
    @Schema(description = "User password", example = "password123")
    val password: String
)
