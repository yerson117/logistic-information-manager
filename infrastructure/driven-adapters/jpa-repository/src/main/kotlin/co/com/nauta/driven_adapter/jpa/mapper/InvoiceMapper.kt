package co.com.nauta.driven_adapter.jpa.mapper

import co.com.nauta.driven_adapter.jpa.entity.InvoiceEntity
import co.com.nauta.model.bo.Invoice
import org.springframework.stereotype.Component

/**
 * Mapper between Invoice domain entity (BO) and InvoiceEntity (JPA Entity)
 * Following hexagonal architecture principles
 */
@Component
class InvoiceMapper {
    
    fun toDomain(invoiceEntity: InvoiceEntity): Invoice {
        return Invoice().apply {
            uuid = invoiceEntity.invoiceId
            clientId = invoiceEntity.clientId
            orderId = invoiceEntity.orderId
            invoiceCode = invoiceEntity.invoiceCode
            amount = invoiceEntity.amount
            createdAt = invoiceEntity.createdAt
            updatedAt = invoiceEntity.updatedAt
        }
    }
    
    fun toEntity(invoice: Invoice): InvoiceEntity {
        return InvoiceEntity(
            invoiceId = invoice.uuid,
            clientId = invoice.clientId ?: throw IllegalArgumentException("Invoice clientId cannot be null"),
            orderId = invoice.orderId,
            invoiceCode = invoice.invoiceCode ?: throw IllegalArgumentException("Invoice code cannot be null"),
            amount = invoice.amount ?: throw IllegalArgumentException("Invoice amount cannot be null"),
            createdAt = invoice.createdAt ?: throw IllegalArgumentException("Invoice createdAt cannot be null"),
            updatedAt = invoice.updatedAt ?: throw IllegalArgumentException("Invoice updatedAt cannot be null")
        )
    }
}
