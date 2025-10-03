package co.com.nauta.rest_api.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.UUID

@Schema(description = "Response DTO for client information")
data class ClientResponseDto(
    @Schema(description = "Client unique identifier", example = "550e8400-e29b-41d4-a716-446655440000")
    val clientId: UUID,
    
    @Schema(description = "Client name", example = "Acme Corporation")
    val name: String,
    
    @Schema(description = "Client email", example = "contact@acme.com")
    val email: String,
    
    @Schema(description = "Creation timestamp", example = "2025-01-01T10:00:00")
    val createdAt: LocalDateTime,
    
    @Schema(description = "Last update timestamp", example = "2025-01-01T10:00:00")
    val updatedAt: LocalDateTime
)
