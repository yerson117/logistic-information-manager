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

@Schema(description = "Información de un contenedor")
data class ContainerRequestDto(
    @JsonProperty("container")
    @Schema(description = "Código único del contenedor", example = "MEDU1234567", required = true)
    val container: String,
    
    @JsonProperty("relatedOrders")
    @Schema(description = "Lista de códigos de órdenes relacionadas (opcional)", example = "[\"PO123\", \"PO456\"]")
    val relatedOrders: List<String>? = null
)

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

@Schema(description = "Información de una factura")
data class InvoiceRequestDto(
    @JsonProperty("invoice")
    @Schema(description = "Código único de la factura", example = "IN123", required = true)
    val invoice: String
)