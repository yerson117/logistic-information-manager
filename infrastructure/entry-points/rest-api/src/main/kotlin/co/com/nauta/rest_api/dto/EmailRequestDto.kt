package co.com.nauta.rest_api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Request para procesar información logística")
data class EmailRequestDto(
    @JsonProperty("booking")
    @Schema(description = "Código de booking opcional", example = "BK123")
    val booking: String? = null,
    
    @JsonProperty("containers")
    @Schema(description = "Lista de contenedores a procesar")
    val containers: List<ContainerRequestDto> = emptyList(),
    
    @JsonProperty("orders")
    @Schema(description = "Lista de órdenes de compra a procesar")
    val orders: List<OrderRequestDto> = emptyList()
)