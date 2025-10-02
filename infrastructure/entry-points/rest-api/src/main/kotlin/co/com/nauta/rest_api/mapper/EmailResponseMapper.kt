package co.com.nauta.rest_api.mapper

import co.com.nauta.model.bo.Booking
import co.com.nauta.model.bo.Container
import co.com.nauta.model.bo.Invoice
import co.com.nauta.model.bo.Order
import co.com.nauta.rest_api.dto.BookingResponseDto
import co.com.nauta.rest_api.dto.ContainerResponseDto
import co.com.nauta.rest_api.dto.InvoiceResponseDto
import co.com.nauta.rest_api.dto.OrderResponseDto
import co.com.nauta.rest_api.dto.ProcessEmailResponseDto
import org.springframework.stereotype.Component
import java.util.UUID

/**
 * Mapper for converting domain BOs to response DTOs
 * This mapper ensures the domain layer remains clean of DTOs
 */
@Component
class EmailResponseMapper {
    
    /**
     * Convert Booking BO to BookingResponseDto
     */
    fun toBookingResponseDto(booking: Booking): BookingResponseDto {
        return BookingResponseDto(
            bookingId = booking.uuid,
            bookingCode = booking.bookingCode ?: "",
            clientId = booking.clientId ?: UUID.randomUUID(),
            createdAt = booking.createdAt!!,
            updatedAt = booking.updatedAt!!
        )
    }

    /**
     * Convert Container BO to ContainerResponseDto
     */
    fun toContainerResponseDto(container: Container): ContainerResponseDto {
        return ContainerResponseDto(
            containerId = container.uuid,
            containerCode = container.containerCode ?: "",
            clientId = container.clientId ?: UUID.randomUUID(),
            bookingId = container.bookingId,
            createdAt = container.createdAt,
            updatedAt = container.updatedAt,
        )
    }

    /**
     * Convert Order BO to OrderResponseDto
     */
    fun toOrderResponseDto(order: Order, invoices: List<Invoice>): OrderResponseDto {
        return OrderResponseDto(
            orderId = order.uuid,
            purchaseCode = order.purchaseCode ?: "",
            clientId = order.clientId ?: UUID.randomUUID(),
            bookingId = order.bookingId,
            createdAt = order.createdAt,
            updatedAt = order.updatedAt,
            invoices = invoices.map { toInvoiceResponseDto(it) }
        )
    }

    /**
     * Convert Invoice BO to InvoiceResponseDto
     */
    fun toInvoiceResponseDto(invoice: Invoice): InvoiceResponseDto {
        return InvoiceResponseDto(
            invoiceId = invoice.uuid,
            invoiceCode = invoice.invoiceCode ?: "",
            clientId = invoice.clientId ?: UUID.randomUUID(),
            orderId = invoice.orderId,
            amount = invoice.amount?.toString() ?: "0",
            createdAt = invoice.createdAt,
            updatedAt = invoice.updatedAt
        )
    }
    
    /**
     * Convert domain BOs to ProcessEmailResponseDto
     * @param booking The booking BO
     * @param containers The list of container BOs
     * @param orders The list of order BOs with their invoices
     * @param clientId The client ID
     * @return ProcessEmailResponseDto for the REST API response
     */
    fun toResponseDto(
        booking: Booking?,
        containers: List<Container>,
        orders: List<Pair<Order, List<Invoice>>>,
        clientId: UUID
    ): ProcessEmailResponseDto {
        val bookingDto = booking?.let { toBookingResponseDto(it) }
        
        val containerDtos = containers.map { toContainerResponseDto(it) }
        
        val orderDtos = orders.map { (order, invoices) ->
            toOrderResponseDto(order, invoices)
        }
        
        val allInvoices = orders.flatMap { it.second }
        val invoiceDtos = allInvoices.map { toInvoiceResponseDto(it) }
        
        return ProcessEmailResponseDto(
            booking = bookingDto,
            containers = containerDtos,
            orders = orderDtos,
            invoices = invoiceDtos,
            message = "Email processed successfully"
        )
    }
}
