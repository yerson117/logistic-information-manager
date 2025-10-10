package co.com.nauta.rest_api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Información de un contenedor")
data class ContainerRequestDto(
    @JsonProperty("container")
    @Schema(description = "Código único del contenedor", example = "MEDU1234567", required = true)
    val container: String,
    
    @JsonProperty("relatedOrders")
    @Schema(description = "Lista de códigos de órdenes relacionadas (opcional)", example = "[\"PO123\", \"PO456\"]")
    val relatedOrders: List<String>? = null
)
