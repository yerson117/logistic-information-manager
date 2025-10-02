package co.com.nauta.usecase

import co.com.nauta.model.bo.Container
import co.com.nauta.model.bo.OrderContainer
import co.com.nauta.usecase.port.ContainerPort
import co.com.nauta.usecase.port.OrderContainerPort
import java.time.LocalDateTime
import java.util.UUID

class ProcessContainerUseCase(
    private val containerPort: ContainerPort,
    private val orderContainerPort: OrderContainerPort
) {
    
    fun process(containerCode: String, clientId: UUID, bookingId: UUID? = null): Container {
        val existingContainer = containerPort.findByCode(clientId, containerCode)
        
        return if (existingContainer != null) {
            existingContainer.bookingId = bookingId
            existingContainer.updatedAt = LocalDateTime.now()
            containerPort.save(existingContainer)
        } else {
            val newContainer = Container()
            newContainer.containerCode = containerCode
            newContainer.clientId = clientId
            newContainer.bookingId = bookingId
            newContainer.createdAt = LocalDateTime.now()
            newContainer.updatedAt = LocalDateTime.now()
            containerPort.save(newContainer)
        }
    }
    
    fun processMultiple(containerCodes: List<String>, clientId: UUID, bookingId: UUID? = null): List<Container> {
        return containerCodes.map { containerCode ->
            process(containerCode, clientId, bookingId)
        }
    }
    
    fun associateWithOrders(containerId: UUID, orderIds: List<UUID>) {
        orderIds.forEach { orderId ->
            createOrderContainerRelation(orderId, containerId)
        }
    }
    
    fun getAssociatedOrders(containerId: UUID): List<OrderContainer> {
        return orderContainerPort.findByContainer(containerId)
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
