package co.com.nauta.usecase

import co.com.nauta.model.bo.Container
import co.com.nauta.model.bo.Invoice
import co.com.nauta.model.bo.Order
import co.com.nauta.usecase.exception.EntityNotFoundException
import co.com.nauta.usecase.port.ContainerPort
import co.com.nauta.usecase.port.InvoicePort
import co.com.nauta.usecase.port.OrderPort
import co.com.nauta.usecase.port.Page
import co.com.nauta.usecase.port.Pageable
import java.util.UUID

/**
 * Use case for retrieving orders and related entities
 */
class GetOrdersUseCase(
    private val orderPort: OrderPort,
    private val containerPort: ContainerPort,
    private val invoicePort: InvoicePort
) {
    
    /**
     * Get all orders for a specific client
     */
    fun getOrdersByClient(clientId: UUID): List<Order> {
        return orderPort.findByClient(clientId)
    }
    
    /**
     * Get orders for a specific client with pagination
     */
    fun getOrdersByClient(clientId: UUID, pageable: Pageable): Page<Order> {
        return orderPort.findByClient(clientId, pageable)
    }
    
    /**
     * Get a specific order by ID
     */
    fun getOrderById(orderId: UUID): Order? {
        return orderPort.findById(orderId)
    }
    
    /**
     * Get order by purchase code for a specific client, throwing exception if not found
     */
    fun getOrderByPurchaseCodeOrThrow(clientId: UUID, purchaseCode: String): Order {
        return orderPort.findByPurchaseCode(clientId, purchaseCode)
            ?: throw EntityNotFoundException("Order", purchaseCode)
    }
    
    /**
     * Get orders by booking ID
     */
    fun getOrdersByBooking(bookingId: UUID): List<Order> {
        return orderPort.findByBooking(bookingId)
    }
    
    /**
     * Get orders by container ID
     */
    fun getOrdersByContainer(containerId: UUID): List<Order> {
        return orderPort.findByContainer(containerId)
    }
    
    /**
     * Get containers associated with a specific order
     */
    fun getContainersByOrder(orderId: UUID): List<Container> {
        return containerPort.findByOrder(orderId)
    }
    
    /**
     * Get containers associated with a specific order with pagination
     */
    fun getContainersByOrder(orderId: UUID, pageable: Pageable): Page<Container> {
        return containerPort.findByOrder(orderId, pageable)
    }
    
    /**
     * Get invoices associated with a specific order
     */
    fun getInvoicesByOrder(orderId: UUID): List<Invoice> {
        return invoicePort.findByOrder(orderId)
    }
}
