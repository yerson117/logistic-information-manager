package co.com.nauta.model.bo

import java.util.UUID

/**
 * ORDER entity representing an order in the logistics system
 * 
 * Attributes:
 * - order_id: UUID, Primary Key
 * - client_id: UUID, Foreign Key to CLIENT
 * - booking_id: UUID, Foreign Key to BOOKING (nullable)
 * - purchase_code: String, unique purchase code (e.g., "PO123")
 * - created_at: LocalDateTime, creation timestamp
 * - updated_at: LocalDateTime, last update timestamp
 * 
 * Relationships:
 * - CLIENT to ORDER: A CLIENT can have multiple orders
 * - BOOKING to ORDER: "puede incluir" (can include). A BOOKING can include zero or many ORDERs
 * - ORDER to INVOICE: An ORDER can have multiple invoices
 * - ORDER to CONTAINER: Many-to-many relationship through OrderContainer
 */
class Order : BaseModelBO() {
    var clientId: UUID? = null
    var bookingId: UUID? = null
    var purchaseCode: String? = null
}
