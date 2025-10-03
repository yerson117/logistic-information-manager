package co.com.nauta.rest_api.mapper

import co.com.nauta.model.bo.Booking
import co.com.nauta.model.bo.Container
import co.com.nauta.model.bo.Invoice
import co.com.nauta.model.bo.Order
import co.com.nauta.rest_api.dto.ContainerRequestDto
import co.com.nauta.rest_api.dto.EmailRequestDto
import co.com.nauta.rest_api.dto.InvoiceRequestDto
import co.com.nauta.rest_api.dto.OrderRequestDto
import co.com.nauta.usecase.ContainerRelation
import co.com.nauta.usecase.OrderRelation
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Component
class EmailRequestMapper {
    
    fun toDomainObjects(emailRequestDto: EmailRequestDto, clientId: UUID): EmailDomainObjects {
        val booking = emailRequestDto.booking?.let { bookingCode ->
            toBookingBO(bookingCode, clientId)
        }
        
        val containers = emailRequestDto.containers.map { containerDto ->
            toContainerBO(containerDto, clientId, booking?.uuid)
        }
        
        val orders = emailRequestDto.orders.map { orderDto ->
            val order = toOrderBO(orderDto, clientId, booking?.uuid)
            val invoices = orderDto.invoices.map { invoiceDto ->
                toInvoiceBO(invoiceDto, clientId, order.uuid)
            }
            order to invoices
        }
        
        // Extract container relations
        val containerRelations = emailRequestDto.containers
            .filter { it.relatedOrders != null && it.relatedOrders.isNotEmpty() }
            .map { containerDto ->
                ContainerRelation(
                    containerCode = containerDto.container,
                    relatedOrderCodes = containerDto.relatedOrders!!
                )
            }
        
        // Extract order relations
        val orderRelations = emailRequestDto.orders
            .filter { it.relatedContainers != null && it.relatedContainers.isNotEmpty() }
            .map { orderDto ->
                OrderRelation(
                    orderCode = orderDto.purchase,
                    relatedContainerCodes = orderDto.relatedContainers!!
                )
            }
        
        return EmailDomainObjects(booking, containers, orders, containerRelations, orderRelations)
    }
    
    fun toBookingBO(bookingCode: String, clientId: UUID): Booking {
        return Booking().apply {
            this.bookingCode = bookingCode
            this.clientId = clientId
            this.createdAt = LocalDateTime.now()
            this.updatedAt = LocalDateTime.now()
        }
    }

    fun toContainerBO(containerDto: ContainerRequestDto, clientId: UUID, bookingId: UUID?): Container {
        return Container().apply {
            this.containerCode = containerDto.container
            this.clientId = clientId
            this.bookingId = bookingId
            this.createdAt = LocalDateTime.now()
            this.updatedAt = LocalDateTime.now()
        }
    }

    fun toOrderBO(orderDto: OrderRequestDto, clientId: UUID, bookingId: UUID?): Order {
        return Order().apply {
            this.purchaseCode = orderDto.purchase
            this.clientId = clientId
            this.bookingId = bookingId
            this.createdAt = LocalDateTime.now()
            this.updatedAt = LocalDateTime.now()
        }
    }

    fun toInvoiceBO(invoiceDto: InvoiceRequestDto, clientId: UUID, orderId: UUID): Invoice {
        return Invoice().apply {
            this.invoiceCode = invoiceDto.invoice
            this.clientId = clientId
            this.orderId = orderId
            this.amount = BigDecimal.ZERO
            this.createdAt = LocalDateTime.now()
            this.updatedAt = LocalDateTime.now()
        }
    }
}

data class EmailDomainObjects(
    val booking: Booking?,
    val containers: List<Container>,
    val orders: List<Pair<Order, List<Invoice>>>,
    val containerRelations: List<ContainerRelation>,
    val orderRelations: List<OrderRelation>
)

