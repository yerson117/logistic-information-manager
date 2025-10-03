package co.com.nauta.rest_api.exception

import co.com.nauta.rest_api.dto.ErrorResponseDto
import co.com.nauta.usecase.exception.BusinessRuleViolationException
import co.com.nauta.usecase.exception.ClientAccessDeniedException
import co.com.nauta.usecase.exception.DomainException
import co.com.nauta.usecase.exception.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.util.UUID

/**
 * Global exception handler for REST API
 * Provides consistent error responses across all endpoints
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    /**
     * Handle domain exceptions
     */
    @ExceptionHandler(DomainException::class)
    fun handleDomainException(ex: DomainException, request: WebRequest): ResponseEntity<ErrorResponseDto> {
        logger.warn("Domain exception: ${ex.message}", ex)
        
        return when (ex) {
            is EntityNotFoundException -> {
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponseDto(
                        status = HttpStatus.NOT_FOUND.value(),
                        error = "Entity Not Found",
                        message = ex.message ?: "Entity not found",
                        path = request.getDescription(false)
                    ))
            }
            is BusinessRuleViolationException -> {
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponseDto(
                        status = HttpStatus.BAD_REQUEST.value(),
                        error = "Business Rule Violation",
                        message = ex.message ?: "Business rule violation",
                        path = request.getDescription(false)
                    ))
            }
            is ClientAccessDeniedException -> {
                ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ErrorResponseDto(
                        status = HttpStatus.FORBIDDEN.value(),
                        error = "Access Denied",
                        message = ex.message ?: "Access denied",
                        path = request.getDescription(false)
                    ))
            }
            else -> {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponseDto(
                        status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        error = "Internal Server Error",
                        message = "An unexpected error occurred",
                        path = request.getDescription(false)
                    ))
            }
        }
    }

    /**
     * Handle validation exceptions
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException, request: WebRequest): ResponseEntity<ErrorResponseDto> {
        logger.warn("Validation exception: ${ex.message}", ex)
        
        val errors = ex.bindingResult.fieldErrors.associate { fieldError ->
            fieldError.field to (fieldError.defaultMessage ?: "Invalid value")
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponseDto(
                status = HttpStatus.BAD_REQUEST.value(),
                error = "Validation Failed",
                message = "Request validation failed",
                path = request.getDescription(false),
                details = errors
            ))
    }

    /**
     * Handle malformed JSON
     */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException, request: WebRequest): ResponseEntity<ErrorResponseDto> {
        logger.warn("Malformed JSON: ${ex.message}", ex)
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponseDto(
                status = HttpStatus.BAD_REQUEST.value(),
                error = "Malformed JSON",
                message = "Request body is not valid JSON",
                path = request.getDescription(false)
            ))
    }

    /**
     * Handle illegal argument exceptions
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException, request: WebRequest): ResponseEntity<ErrorResponseDto> {
        logger.warn("Illegal argument: ${ex.message}", ex)
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponseDto(
                status = HttpStatus.BAD_REQUEST.value(),
                error = "Invalid Argument",
                message = ex.message ?: "Invalid argument provided",
                path = request.getDescription(false)
            ))
    }

    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponseDto> {
        val errorId = UUID.randomUUID().toString()
        logger.error("Unexpected error [ID: $errorId]: ${ex.message}", ex)
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponseDto(
                status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                error = "Internal Server Error",
                message = "An unexpected error occurred. Error ID: $errorId",
                path = request.getDescription(false)
            ))
    }
}
