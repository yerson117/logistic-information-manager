package co.com.nauta.driven_adapter.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

/**
 * JPA Entity for Booking
 * This represents the database table structure for Booking domain entity
 */
@Entity
@Table(name = "bookings")
data class BookingEntity(
    @Id
    @Column(name = "booking_id")
    val bookingId: UUID,
    
    @Column(name = "client_id", nullable = false)
    val clientId: UUID,
    
    @Column(name = "booking_code", nullable = false)
    val bookingCode: String,
    
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,
    
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime
) {
    // Default constructor for JPA
    constructor() : this(UUID.randomUUID(), UUID.randomUUID(), "", LocalDateTime.now(), LocalDateTime.now())
}
