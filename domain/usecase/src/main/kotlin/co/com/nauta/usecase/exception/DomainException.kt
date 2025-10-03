package co.com.nauta.usecase.exception

/**
 * Base exception for domain layer
 * This keeps the domain layer clean of framework dependencies
 */
abstract class DomainException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

/**
 * Exception thrown when a required entity is not found
 */
class EntityNotFoundException(
    entityType: String,
    entityId: String,
    cause: Throwable? = null
) : DomainException("$entityType with ID $entityId not found", cause)

/**
 * Exception thrown when business rules are violated
 */
class BusinessRuleViolationException(
    message: String,
    cause: Throwable? = null
) : DomainException(message, cause)

/**
 * Exception thrown when client access is denied
 */
class ClientAccessDeniedException(
    clientId: String,
    resourceId: String,
    cause: Throwable? = null
) : DomainException("Client $clientId does not have access to resource $resourceId", cause)
