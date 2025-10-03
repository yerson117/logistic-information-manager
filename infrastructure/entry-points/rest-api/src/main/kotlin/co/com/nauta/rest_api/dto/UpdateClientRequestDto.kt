package co.com.nauta.rest_api.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

@Schema(description = "Request DTO for updating a client")
data class UpdateClientRequestDto(
    @field:Size(min = 2, max = 100, message = "Client name must be between 2 and 100 characters")
    @Schema(description = "Client name", example = "Acme Corporation Updated")
    val name: String? = null,
    
    @field:Email(message = "Client email must be valid")
    @Schema(description = "Client email", example = "newcontact@acme.com")
    val email: String? = null
)
