package co.com.nauta.driven_adapter.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

/**
 * JPA Entity for OrderContainer (junction table)
 * This represents the many-to-many relationship between Order and Container
 */
@Entity
@Table(name = "order_containers")
@IdClass(OrderContainerId::class)
data class OrderContainerEntity(
    @Id
    @Column(name = "order_id")
    val orderId: UUID,
    
    @Id
    @Column(name = "container_id")
    val containerId: UUID,
    
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,
    
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime
) {
    // Default constructor for JPA
    constructor() : this(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now())
}
