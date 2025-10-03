package co.com.nauta.driven_adapter.jpa.entity

import java.io.Serializable
import java.util.UUID

/**
 * Composite ID class for OrderContainerEntity
 * Required for JPA entities with composite primary keys using @IdClass
 */
data class OrderContainerId(
    val orderId: UUID,
    val containerId: UUID
) : Serializable {
    // Default constructor for JPA
    constructor() : this(UUID.randomUUID(), UUID.randomUUID())
}
