package co.com.nauta.usecase

import co.com.nauta.model.bo.Container
import co.com.nauta.model.bo.Order
import co.com.nauta.usecase.port.ContainerPort
import co.com.nauta.usecase.port.OrderPort
import co.com.nauta.usecase.port.Page
import co.com.nauta.usecase.port.Pageable
import java.util.UUID

/**
 * Use case for retrieving containers and related entities
 */
class GetContainersUseCase(
    private val containerPort: ContainerPort,
    private val orderPort: OrderPort
) {
    
    /**
     * Get all containers for a specific client
     */
    fun getContainersByClient(clientId: UUID): List<Container> {
        return containerPort.findByClient(clientId)
    }
    
    /**
     * Get containers for a specific client with pagination
     */
    fun getContainersByClient(clientId: UUID, pageable: Pageable): Page<Container> {
        return containerPort.findByClient(clientId, pageable)
    }
    
    /**
     * Get a specific container by ID
     */
    fun getContainerById(containerId: UUID): Container? {
        return containerPort.findById(containerId)
    }
    
    /**
     * Get container by code for a specific client
     */
    fun getContainerByCode(clientId: UUID, containerCode: String): Container? {
        return containerPort.findByCode(clientId, containerCode)
    }
    
    /**
     * Get containers by booking ID
     */
    fun getContainersByBooking(bookingId: UUID): List<Container> {
        return containerPort.findByBooking(bookingId)
    }
    
    /**
     * Get containers by order ID
     */
    fun getContainersByOrder(orderId: UUID): List<Container> {
        return containerPort.findByOrder(orderId)
    }
    
    /**
     * Get orders associated with a specific container
     */
    fun getOrdersByContainer(containerId: UUID): List<Order> {
        return orderPort.findByContainer(containerId)
    }
    
    /**
     * Get orders associated with a specific container with pagination
     */
    fun getOrdersByContainer(containerId: UUID, pageable: Pageable): Page<Order> {
        return orderPort.findByContainer(containerId, pageable)
    }
}
