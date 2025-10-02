package co.com.nauta.driven_adapter.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

/**
 * JPA Entity for Order
 * This represents the database table structure for Order domain entity
 */
@Entity
@Table(name = "orders")
data class OrderEntity(
    @Id
    @Column(name = "order_id")
    val orderId: UUID,
    
    @Column(name = "client_id", nullable = false)
    val clientId: UUID,
    
    @Column(name = "booking_id")
    val bookingId: UUID?,
    
    @Column(name = "purchase_code", nullable = false)
    val purchaseCode: String,
    
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,
    
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime
) {
    // Default constructor for JPA
    constructor() : this(UUID.randomUUID(), UUID.randomUUID(), null, "", LocalDateTime.now(), LocalDateTime.now())
}
