package co.com.nauta.usecase

import co.com.nauta.model.bo.Order
import co.com.nauta.model.bo.OrderContainer
import co.com.nauta.usecase.port.OrderPort
import co.com.nauta.usecase.port.OrderContainerPort
import java.time.LocalDateTime
import java.util.UUID

class ProcessOrderUseCase(
    private val orderPort: OrderPort,
    private val orderContainerPort: OrderContainerPort
) {
    
    fun process(purchaseCode: String, clientId: UUID, bookingId: UUID? = null): Order {
        val existingOrder = orderPort.findByPurchaseCode(clientId, purchaseCode)
        
        return if (existingOrder != null) {
            existingOrder.bookingId = bookingId
            existingOrder.updatedAt = LocalDateTime.now()
            orderPort.save(existingOrder)
        } else {
            val newOrder = Order()
            newOrder.purchaseCode = purchaseCode
            newOrder.clientId = clientId
            newOrder.bookingId = bookingId
            newOrder.createdAt = LocalDateTime.now()
            newOrder.updatedAt = LocalDateTime.now()
            orderPort.save(newOrder)
        }
    }
    
    fun processMultiple(purchaseCodes: List<String>, clientId: UUID, bookingId: UUID? = null): List<Order> {
        return purchaseCodes.map { purchaseCode ->
            process(purchaseCode, clientId, bookingId)
        }
    }
    
    fun associateWithContainers(orderId: UUID, containerIds: List<UUID>) {
        containerIds.forEach { containerId ->
            createOrderContainerRelation(orderId, containerId)
        }
    }
    
    fun getAssociatedContainers(orderId: UUID): List<OrderContainer> {
        return orderContainerPort.findByOrder(orderId)
    }
    
    private fun createOrderContainerRelation(orderId: UUID, containerId: UUID): OrderContainer {
        val existingRelation = orderContainerPort.findByOrderAndContainer(orderId, containerId)
        
        return if (existingRelation != null) {
            existingRelation.updatedAt = LocalDateTime.now()
            orderContainerPort.save(existingRelation)
        } else {
            val newRelation = OrderContainer()
            newRelation.orderId = orderId
            newRelation.containerId = containerId
            newRelation.createdAt = LocalDateTime.now()
            newRelation.updatedAt = LocalDateTime.now()
            orderContainerPort.save(newRelation)
        }
    }
}
