package co.com.nauta.rest_api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Información de una orden de compra")
data class OrderRequestDto(
    @JsonProperty("purchase")
    @Schema(description = "Código único de la orden de compra", example = "PO123", required = true)
    val purchase: String,
    
    @JsonProperty("relatedContainers")
    @Schema(description = "Lista de códigos de contenedores relacionados (opcional)", example = "[\"MEDU1234567\", \"MEDU7654321\"]")
    val relatedContainers: List<String>? = null,
    
    @JsonProperty("invoices")
    @Schema(description = "Lista de facturas asociadas a la orden")
    val invoices: List<InvoiceRequestDto> = emptyList()
)
