package co.com.nauta.driven_adapter.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

/**
 * JPA Entity for Container
 * This represents the database table structure for Container domain entity
 */
@Entity
@Table(name = "containers")
data class ContainerEntity(
    @Id
    @Column(name = "container_id")
    val containerId: UUID,
    
    @Column(name = "client_id", nullable = false)
    val clientId: UUID,
    
    @Column(name = "booking_id")
    val bookingId: UUID?,
    
    @Column(name = "container_code", nullable = false)
    val containerCode: String,
    
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,
    
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime
) {
    constructor() : this(UUID.randomUUID(), UUID.randomUUID(), null, "", LocalDateTime.now(), LocalDateTime.now())
}
