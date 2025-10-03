package co.com.nauta.rest_api.controller

import co.com.nauta.rest_api.dto.ClientResponseDto
import co.com.nauta.rest_api.dto.CreateClientRequestDto
import co.com.nauta.rest_api.dto.UpdateClientRequestDto
import co.com.nauta.usecase.CreateClientUseCase
import co.com.nauta.usecase.GetClientUseCase
import co.com.nauta.usecase.UpdateClientUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/clients", produces = [MediaType.APPLICATION_JSON_VALUE])
@Tag(name = "Clients", description = "Endpoints para gestión de clientes")
class ClientController(
    private val createClientUseCase: CreateClientUseCase,
    private val getClientUseCase: GetClientUseCase,
    private val updateClientUseCase: UpdateClientUseCase
) {
    
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Crear cliente",
        description = "Crea un nuevo cliente en el sistema"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Cliente creado exitosamente",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ClientResponseDto::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Datos de entrada inválidos"
            ),
            ApiResponse(
                responseCode = "409",
                description = "Cliente con email ya existe"
            )
        ]
    )
    fun createClient(@Valid @RequestBody request: CreateClientRequestDto): ResponseEntity<ClientResponseDto> {
        return try {
            val client = createClientUseCase.execute(request.name, request.email)
            val response = ClientResponseDto(
                clientId = client.uuid,
                name = client.name!!,
                email = client.email!!,
                createdAt = client.createdAt!!,
                updatedAt = client.updatedAt!!
            )
            ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (e: Exception) {
            when (e.message) {
                "Client with email ${request.email} already exists" -> 
                    ResponseEntity.status(HttpStatus.CONFLICT).build()
                else -> 
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
            }
        }
    }
    
    @GetMapping("/{clientId}")
    @Operation(
        summary = "Obtener cliente por ID",
        description = "Obtiene la información de un cliente específico"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Cliente encontrado",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ClientResponseDto::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Cliente no encontrado"
            )
        ]
    )
    fun getClientById(
        @Parameter(description = "ID del cliente")
        @PathVariable clientId: UUID
    ): ResponseEntity<ClientResponseDto> {
        val client = getClientUseCase.getById(clientId)
        return if (client != null) {
            val response = ClientResponseDto(
                clientId = client.uuid,
                name = client.name!!,
                email = client.email!!,
                createdAt = client.createdAt!!,
                updatedAt = client.updatedAt!!
            )
            ResponseEntity.ok(response)
        } else {
            ResponseEntity.notFound().build()
        }
    }
    
    @GetMapping
    @Operation(
        summary = "Listar todos los clientes",
        description = "Obtiene la lista de todos los clientes registrados"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Lista de clientes obtenida exitosamente",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = Array<ClientResponseDto>::class)
                )]
            )
        ]
    )
    fun getAllClients(): ResponseEntity<List<ClientResponseDto>> {
        val clients = getClientUseCase.getAll()
        val response = clients.map { client ->
            ClientResponseDto(
                clientId = client.uuid,
                name = client.name!!,
                email = client.email!!,
                createdAt = client.createdAt!!,
                updatedAt = client.updatedAt!!
            )
        }
        return ResponseEntity.ok(response)
    }
    
    @PutMapping("/{clientId}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Actualizar cliente",
        description = "Actualiza la información de un cliente existente"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Cliente actualizado exitosamente",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ClientResponseDto::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Datos de entrada inválidos"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Cliente no encontrado"
            ),
            ApiResponse(
                responseCode = "409",
                description = "Email ya existe en otro cliente"
            )
        ]
    )
    fun updateClient(
        @Parameter(description = "ID del cliente")
        @PathVariable clientId: UUID,
        @Valid @RequestBody request: UpdateClientRequestDto
    ): ResponseEntity<ClientResponseDto> {
        return try {
            val client = updateClientUseCase.execute(clientId, request.name, request.email)
            val response = ClientResponseDto(
                clientId = client.uuid,
                name = client.name!!,
                email = client.email!!,
                createdAt = client.createdAt!!,
                updatedAt = client.updatedAt!!
            )
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            when (e.message) {
                "Client with ID $clientId not found" -> 
                    ResponseEntity.notFound().build()
                "Client with email ${request.email} already exists" -> 
                    ResponseEntity.status(HttpStatus.CONFLICT).build()
                else -> 
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
            }
        }
    }
}
