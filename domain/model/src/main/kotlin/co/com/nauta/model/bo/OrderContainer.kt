package co.com.nauta.model.bo

import java.util.UUID

/**
 * ORDER_CONTAINER entity representing the many-to-many relationship between ORDER and CONTAINER
 * 
 * Attributes:
 * - order_id: UUID, Foreign Key to ORDER
 * - container_id: UUID, Foreign Key to CONTAINER
 * - created_at: LocalDateTime, creation timestamp
 * - updated_at: LocalDateTime, last update timestamp
 * 
 * Relationships:
 * - CONTAINER to ORDER_CONTAINER: "relación" (relationship). A CONTAINER can be associated with zero or many ORDER_CONTAINER entries
 * - ORDER to ORDER_CONTAINER: "relación" (relationship). An ORDER can be associated with zero or many ORDER_CONTAINER entries
 * 
 * This entity serves as a junction table, implying a many-to-many relationship between ORDER and CONTAINER
 */
class OrderContainer : BaseModelBO() {
    var orderId: UUID? = null
    var containerId: UUID? = null
}
