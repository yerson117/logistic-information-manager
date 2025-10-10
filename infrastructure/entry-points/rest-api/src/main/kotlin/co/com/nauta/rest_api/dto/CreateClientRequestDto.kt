package co.com.nauta.rest_api.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "Request DTO for creating a new client")
data class CreateClientRequestDto(
    @field:NotBlank(message = "Client name is required")
    @field:Size(min = 2, max = 100, message = "Client name must be between 2 and 100 characters")
    @Schema(description = "Client name", example = "Acme Corporation")
    val name: String,
    
    @field:NotBlank(message = "Client email is required")
    @field:Email(message = "Client email must be valid")
    @Schema(description = "Client email", example = "contact@acme.com")
    val email: String
)
