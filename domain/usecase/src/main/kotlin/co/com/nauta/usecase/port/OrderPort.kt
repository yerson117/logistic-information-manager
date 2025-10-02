package co.com.nauta.usecase.port

import co.com.nauta.model.bo.Order
import java.util.UUID

/**
 * Port for Order operations
 * Following hexagonal architecture principles - one port per entity
 */
interface OrderPort {
    fun save(order: Order): Order
    fun findById(orderId: UUID): Order?
    fun findByPurchaseCode(clientId: UUID, purchaseCode: String): Order?
    fun findByClient(clientId: UUID): List<Order>
    fun findByClient(clientId: UUID, pageable: Pageable): Page<Order>
    fun findByBooking(bookingId: UUID): List<Order>
    fun findByContainer(containerId: UUID): List<Order>
    fun findByContainer(containerId: UUID, pageable: Pageable): Page<Order>
}
