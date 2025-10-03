package co.com.nauta.model.bo

import java.util.UUID

/**
 * CONTAINER entity representing a container in the logistics system
 * 
 * Attributes:
 * - container_id: UUID, Primary Key
 * - client_id: UUID, Foreign Key to CLIENT
 * - booking_id: UUID, Foreign Key to BOOKING (nullable)
 * - container_code: String, unique container code (e.g., "MEDU1234567")
 * - created_at: LocalDateTime, creation timestamp
 * - updated_at: LocalDateTime, last update timestamp
 * 
 * Relationships:
 * - CLIENT to CONTAINER: A CLIENT can have multiple containers
 * - BOOKING to CONTAINER: "puede contener" (can contain). A BOOKING can contain zero or many CONTAINERs
 * - CONTAINER to ORDER: Many-to-many relationship through OrderContainer
 */
class Container : BaseModelBO() {
    var clientId: UUID? = null
    var bookingId: UUID? = null
    var containerCode: String? = null
}
