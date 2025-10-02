package co.com.nauta.model.bo

import java.time.LocalDateTime
import java.util.UUID

/**
 * Base entity with common audit fields
 * All entities in the system should extend from this base class
 */
abstract class BaseModelBO {
    open var uuid: UUID = UUID.randomUUID()
    open var createdAt: LocalDateTime? = null
    open var updatedAt: LocalDateTime? = null
}
