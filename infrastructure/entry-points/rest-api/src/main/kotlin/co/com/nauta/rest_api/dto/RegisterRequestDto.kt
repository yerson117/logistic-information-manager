package co.com.nauta.rest_api.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

@Schema(description = "Request DTO for user registration")
data class RegisterRequestDto(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email must be valid")
    @Schema(description = "User email", example = "user@example.com")
    val email: String,
    
    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(description = "User password", example = "password123")
    val password: String,
    
    @Schema(description = "Client ID to associate with the user", example = "550e8400-e29b-41d4-a716-446655440000")
    val clientId: UUID
)
