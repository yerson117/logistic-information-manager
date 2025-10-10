package co.com.nauta.rest_api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Información de una factura")
data class InvoiceRequestDto(
    @JsonProperty("invoice")
    @Schema(description = "Código único de la factura", example = "IN123", required = true)
    val invoice: String
)
