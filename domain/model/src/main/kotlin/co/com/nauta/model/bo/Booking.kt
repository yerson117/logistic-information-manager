package co.com.nauta.model.bo

import java.util.UUID

/**
 * BOOKING entity representing a booking in the logistics system
 * 
 * Attributes:
 * - booking_id: UUID, Primary Key
 * - client_id: UUID, Foreign Key to CLIENT
 * - booking_code: String, unique booking code (e.g., "BK123")
 * - created_at: LocalDateTime, creation timestamp
 * - updated_at: LocalDateTime, last update timestamp
 * 
 * Relationships:
 * - CLIENT to BOOKING: "tiene" (has). A CLIENT can have one or many BOOKINGs
 * - BOOKING to CONTAINER: A BOOKING can have multiple containers
 */
class Booking : BaseModelBO() {
    var clientId: UUID? = null
    var bookingCode: String? = null
}
