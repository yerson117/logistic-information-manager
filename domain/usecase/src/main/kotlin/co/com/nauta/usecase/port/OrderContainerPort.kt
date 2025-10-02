package co.com.nauta.usecase.port

import co.com.nauta.model.bo.OrderContainer
import java.util.UUID

/**
 * Port for OrderContainer operations (junction table)
 * Following hexagonal architecture principles - one port per entity
 */
interface OrderContainerPort {
    fun save(orderContainer: OrderContainer): OrderContainer
    fun findByOrderAndContainer(orderId: UUID, containerId: UUID): OrderContainer?
    fun findByOrder(orderId: UUID): List<OrderContainer>
    fun findByContainer(containerId: UUID): List<OrderContainer>
    fun deleteOrderContainer(orderId: UUID, containerId: UUID)
}
