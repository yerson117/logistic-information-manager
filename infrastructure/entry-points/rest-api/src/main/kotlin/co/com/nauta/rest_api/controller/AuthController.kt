package co.com.nauta.rest_api.controller

import co.com.nauta.rest_api.dto.AuthRequestDto
import co.com.nauta.rest_api.dto.AuthResponseDto
import co.com.nauta.rest_api.dto.RegisterRequestDto
import co.com.nauta.rest_api.service.JwtService
import co.com.nauta.usecase.AuthenticationUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/auth", produces = [MediaType.APPLICATION_JSON_VALUE])
@Tag(name = "Authentication", description = "Endpoints para autenticación y generación de tokens JWT")
class AuthController(
    private val jwtService: JwtService,
    private val authenticationUseCase: AuthenticationUseCase
) {

    @PostMapping("/login", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica un usuario con email y contraseña, genera un token JWT."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Autenticación exitosa",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = AuthResponseDto::class)
                )]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Credenciales inválidas"
            )
        ]
    )
    fun login(@Valid @RequestBody authRequest: AuthRequestDto): ResponseEntity<AuthResponseDto> {
        return try {
            val user = authenticationUseCase.authenticate(
                authRequest.email, 
                authRequest.password
            )
            
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }
            
            val clientId = user.clientId!!
            val token = jwtService.generateToken(clientId)
            val checksum = jwtService.extractChecksum(token)
            
            val response = AuthResponseDto(
                token = token,
                expiresIn = jwtService.getExpirationTime(),
                userId = clientId,
                checksum = checksum
            )
            
            ResponseEntity.ok(response)
            
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    @PostMapping("/register", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Registrar usuario",
        description = "Registra un nuevo usuario con email, contraseña y clientId."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Usuario registrado exitosamente",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = AuthResponseDto::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Datos de registro inválidos"
            ),
            ApiResponse(
                responseCode = "409",
                description = "Usuario ya existe"
            )
        ]
    )
    fun register(@Valid @RequestBody registerRequest: RegisterRequestDto): ResponseEntity<AuthResponseDto> {
        return try {
            val user = authenticationUseCase.register(
                registerRequest.email,
                registerRequest.password,
                registerRequest.clientId
            )
            
            val clientId = user.clientId!!
            val token = jwtService.generateToken(clientId)
            val checksum = jwtService.extractChecksum(token)
            
            val response = AuthResponseDto(
                token = token,
                expiresIn = jwtService.getExpirationTime(),
                userId = clientId,
                checksum = checksum
            )
            
            ResponseEntity.ok(response)
            
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @PostMapping("/validate")
    fun validateToken(@RequestBody request: Map<String, String>): ResponseEntity<Map<String, Any>> {
        return try {
            val token = request["token"]
            if (token.isNullOrBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
            }
            
            val userId = jwtService.extractUserId(token)
            val checksum = jwtService.extractChecksum(token)
            val isValid = jwtService.validateToken(token, userId)
            
            val response = mapOf(
                "valid" to isValid,
                "userId" to userId,
                "checksum" to checksum
            )
            
            if (isValid) {
                ResponseEntity.ok(response)
            } else {
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response)
            }
            
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }
}
