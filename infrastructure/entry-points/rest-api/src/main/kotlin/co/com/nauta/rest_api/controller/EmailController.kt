package co.com.nauta.rest_api.controller

import co.com.nauta.rest_api.dto.EmailRequestDto
import co.com.nauta.rest_api.dto.ProcessEmailResponseDto
import co.com.nauta.rest_api.mapper.EmailRequestMapper
import co.com.nauta.rest_api.mapper.EmailResponseMapper
import co.com.nauta.rest_api.service.JwtService
import co.com.nauta.usecase.ProcessEmailUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/email", produces = [MediaType.APPLICATION_JSON_VALUE])
@Tag(name = "Email Processing", description = "Endpoints para procesar información logística")
class EmailController(
    private val processEmailUseCase: ProcessEmailUseCase,
    private val emailRequestMapper: EmailRequestMapper,
    private val emailResponseMapper: EmailResponseMapper,
    private val jwtService: JwtService
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Procesar información logística",
        description = "Procesa información logística de bookings, contenedores, órdenes de compra e invoices. Permite establecer relaciones entre entidades de forma progresiva."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Información procesada exitosamente",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ProcessEmailResponseDto::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Datos de entrada inválidos",
                content = [Content(
                    mediaType = "application/json"
                )]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Token JWT inválido o expirado"
            ),
            ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor",
                content = [Content(
                    mediaType = "application/json"
                )]
            )
        ]
    )
    @SecurityRequirement(name = "bearerAuth")
    fun processEmail(
        @Parameter(hidden = true) authentication: Authentication,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de información logística a procesar"
        )
        @RequestBody emailRequestDto: EmailRequestDto
    ): ResponseEntity<ProcessEmailResponseDto> {

        return try {
            // Get userId from JWT authentication
            val userId = authentication.name
            val clientUuid = UUID.fromString(userId)
            
            // Convert DTO to domain objects using MapStruct
            val domainObjects = emailRequestMapper.toDomainObjects(emailRequestDto, clientUuid)
            
            // Process using domain objects
            val result = processEmailUseCase.processEmail(
                domainObjects.booking,
                domainObjects.containers,
                domainObjects.orders,
                domainObjects.containerRelations,
                domainObjects.orderRelations
            )
            
            // Convert domain result to response DTO using MapStruct
            val responseDto = emailResponseMapper.toResponseDto(
                result.booking,
                result.containers,
                result.orders,
                clientUuid
            )
            
            ResponseEntity.ok(responseDto)
            
                } catch (e: IllegalArgumentException) {
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ProcessEmailResponseDto(message = "Invalid user ID format")
                    )
                } catch (e: Exception) {
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        ProcessEmailResponseDto(message = "Internal server error: ${e.message}")
                    )
                }
    }
}