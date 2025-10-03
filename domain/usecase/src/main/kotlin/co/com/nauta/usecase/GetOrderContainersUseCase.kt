package co.com.nauta.usecase

import co.com.nauta.model.bo.Container
import co.com.nauta.model.bo.Order
import co.com.nauta.usecase.port.ContainerPort
import co.com.nauta.usecase.port.OrderPort
import java.util.UUID

/**
 * Use case for retrieving containers associated with a specific order
 */
class GetOrderContainersUseCase(
    private val orderPort: OrderPort,
    private val containerPort: ContainerPort
) {
    
    /**
     * Get all containers associated with a specific order
     */
    fun getContainersByOrder(orderId: UUID): List<Container> {
        // First verify that the order exists
        val order = orderPort.findById(orderId)
        if (order == null) {
            return emptyList()
        }
        
        // Get containers associated with this order
        return containerPort.findByOrder(orderId)
    }
    
    /**
     * Get order and its associated containers
     */
    fun getOrderWithContainers(orderId: UUID): Pair<Order?, List<Container>> {
        val order = orderPort.findById(orderId)
        val containers = if (order != null) {
            containerPort.findByOrder(orderId)
        } else {
            emptyList()
        }
        
        return Pair(order, containers)
    }
}
