package co.com.nauta.usecase

import co.com.nauta.model.bo.Booking
import co.com.nauta.model.bo.Container
import co.com.nauta.model.bo.Invoice
import co.com.nauta.model.bo.Order
import co.com.nauta.usecase.exception.BusinessRuleViolationException
import co.com.nauta.usecase.port.ContainerPort
import co.com.nauta.usecase.port.OrderPort
import lombok.RequiredArgsConstructor
import java.time.LocalDateTime
import java.util.UUID

@RequiredArgsConstructor
class ProcessEmailUseCase(
    private val processBookingUseCase: ProcessBookingUseCase,
    private val processContainerUseCase: ProcessContainerUseCase,
    private val processOrderUseCase: ProcessOrderUseCase,
    private val processInvoiceUseCase: ProcessInvoiceUseCase,
    private val containerPort: ContainerPort,
    private val orderPort: OrderPort
) {
    
    fun processEmail(
        booking: Booking?,
        containers: List<Container>,
        orders: List<Pair<Order, List<Invoice>>>,
        containerRelations: List<ContainerRelation> = emptyList(),
        orderRelations: List<OrderRelation> = emptyList()
    ): ProcessEmailResult {
        
        validateNoDuplicates(booking, containers, orders)
        validateDataConsistency(booking, containers, orders)
        
        val processedBooking = booking?.let { 
            processBookingUseCase.process(it.bookingCode ?: "", it.clientId ?: UUID.randomUUID())
        }
        
        val processedContainers = containers.map { container ->
            processContainerUseCase.process(
                container.containerCode ?: "",
                container.clientId ?: UUID.randomUUID(),
                processedBooking?.uuid
            )
        }
        
        val processedOrders = orders.map { (order, invoices) ->
            val processedOrder = processOrderUseCase.process(
                order.purchaseCode ?: "",
                order.clientId ?: UUID.randomUUID(),
                processedBooking?.uuid
            )
            
            val processedInvoices = invoices.map { invoice ->
                processInvoiceUseCase.process(
                    invoice.invoiceCode ?: "",
                    invoice.clientId ?: UUID.randomUUID(),
                    processedOrder.uuid
                )
            }
            
            processedOrder to processedInvoices
        }
        
        val clientId = getClientId(booking, containers, orders)
        processContainerRelations(clientId, containerRelations)
        processOrderRelations(clientId, orderRelations)
        
        if (processedContainers.isNotEmpty() && processedOrders.isNotEmpty()) {
            val orderEntities = processedOrders.map { it.first }
            val containerEntities = processedContainers
            
            orderEntities.forEach { order ->
                val containerIds = containerEntities.map { it.uuid }
                processOrderUseCase.associateWithContainers(order.uuid, containerIds)
            }
        }
        
        return ProcessEmailResult(
            booking = processedBooking,
            containers = processedContainers,
            orders = processedOrders
        )
    }
    
    private fun validateNoDuplicates(
        booking: Booking?,
        containers: List<Container>,
        orders: List<Pair<Order, List<Invoice>>>
    ) {
        booking?.bookingCode?.let { bookingCode ->
            if (bookingCode.isBlank()) {
                throw BusinessRuleViolationException("Booking code cannot be blank")
            }
        }
        
        val containerCodes = containers.mapNotNull { it.containerCode }
        val duplicateContainers = containerCodes.groupingBy { it }.eachCount().filter { it.value > 1 }
        if (duplicateContainers.isNotEmpty()) {
            throw BusinessRuleViolationException("Duplicate container codes found: ${duplicateContainers.keys}")
        }
        
        val purchaseCodes = orders.mapNotNull { it.first.purchaseCode }
        val duplicateOrders = purchaseCodes.groupingBy { it }.eachCount().filter { it.value > 1 }
        if (duplicateOrders.isNotEmpty()) {
            throw BusinessRuleViolationException("Duplicate purchase codes found: ${duplicateOrders.keys}")
        }
        
        orders.forEach { (order, invoices) ->
            val invoiceCodes = invoices.mapNotNull { it.invoiceCode }
            val duplicateInvoices = invoiceCodes.groupingBy { it }.eachCount().filter { it.value > 1 }
            if (duplicateInvoices.isNotEmpty()) {
                throw BusinessRuleViolationException("Duplicate invoice codes found in order ${order.purchaseCode}: ${duplicateInvoices.keys}")
            }
        }
    }
    
    private fun validateDataConsistency(
        booking: Booking?,
        containers: List<Container>,
        orders: List<Pair<Order, List<Invoice>>>
    ) {
        val clientIds = mutableSetOf<UUID>()
        
        booking?.clientId?.let { clientIds.add(it) }
        containers.forEach { it.clientId?.let { clientId -> clientIds.add(clientId) } }
        orders.forEach { (order, invoices) ->
            order.clientId?.let { clientIds.add(it) }
            invoices.forEach { it.clientId?.let { clientId -> clientIds.add(clientId) } }
        }
        
        if (clientIds.size > 1) {
            throw BusinessRuleViolationException("All entities must belong to the same client. Found clients: ${clientIds.map { it.toString() }}")
        }
        
        if (clientIds.isEmpty()) {
            throw BusinessRuleViolationException("At least one entity must have a valid client ID")
        }
    }
    
    private fun getClientId(
        booking: Booking?,
        containers: List<Container>,
        orders: List<Pair<Order, List<Invoice>>>
    ): UUID {
        booking?.clientId?.let { return it }
        containers.firstOrNull()?.clientId?.let { return it }
        orders.firstOrNull()?.first?.clientId?.let { return it }
        throw BusinessRuleViolationException("No valid client ID found")
    }
    
    private fun processContainerRelations(clientId: UUID, containerRelations: List<ContainerRelation>) {
        containerRelations.forEach { relation ->
            val container = containerPort.findByCode(clientId, relation.containerCode)
            if (container != null) {
                val orderIds = relation.relatedOrderCodes.map { orderCode ->
                    val existingOrder = orderPort.findByPurchaseCode(clientId, orderCode)
                    if (existingOrder != null) {
                        existingOrder.uuid
                    } else {
                        createBasicOrder(orderCode, clientId).uuid
                    }
                }
                processContainerUseCase.associateWithOrders(container.uuid, orderIds)
            }
        }
    }
    
    private fun processOrderRelations(clientId: UUID, orderRelations: List<OrderRelation>) {
        orderRelations.forEach { relation ->
            val order = orderPort.findByPurchaseCode(clientId, relation.orderCode)
            if (order != null) {
                val containerIds = relation.relatedContainerCodes.map { containerCode ->
                    val existingContainer = containerPort.findByCode(clientId, containerCode)
                    if (existingContainer != null) {
                        existingContainer.uuid
                    } else {
                        createBasicContainer(containerCode, clientId).uuid
                    }
                }
                processOrderUseCase.associateWithContainers(order.uuid, containerIds)
            }
        }
    }
    
    private fun createBasicOrder(orderCode: String, clientId: UUID): Order {
        val newOrder = Order()
        newOrder.purchaseCode = orderCode
        newOrder.clientId = clientId
        newOrder.createdAt = LocalDateTime.now()
        newOrder.updatedAt = LocalDateTime.now()
        return orderPort.save(newOrder)
    }
    
    private fun createBasicContainer(containerCode: String, clientId: UUID): Container {
        val newContainer = Container()
        newContainer.containerCode = containerCode
        newContainer.clientId = clientId
        newContainer.createdAt = LocalDateTime.now()
        newContainer.updatedAt = LocalDateTime.now()
        return containerPort.save(newContainer)
    }
}

data class ProcessEmailResult(
    val booking: Booking?,
    val containers: List<Container>,
    val orders: List<Pair<Order, List<Invoice>>>
)

data class ContainerRelation(
    val containerCode: String,
    val relatedOrderCodes: List<String>
)

data class OrderRelation(
    val orderCode: String,
    val relatedContainerCodes: List<String>
)