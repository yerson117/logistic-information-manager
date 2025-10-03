package co.com.nauta.driven_adapter.jpa.adapter

import co.com.nauta.driven_adapter.jpa.mapper.InvoiceMapper
import co.com.nauta.driven_adapter.jpa.repository.InvoiceJpaRepository
import co.com.nauta.model.bo.Invoice
import co.com.nauta.usecase.port.InvoicePort
import org.springframework.stereotype.Component
import java.util.UUID

/**
 * JPA Adapter implementation for InvoicePort
 * Following hexagonal architecture principles - one adapter per port
 */
@Component
class InvoiceJpaAdapter(
    private val invoiceJpaRepository: InvoiceJpaRepository,
    private val invoiceMapper: InvoiceMapper
) : InvoicePort {
    
    override fun save(invoice: Invoice): Invoice {
        val invoiceEntity = invoiceMapper.toEntity(invoice)
        val savedEntity = invoiceJpaRepository.save(invoiceEntity)
        return invoiceMapper.toDomain(savedEntity)
    }
    
    override fun findById(invoiceId: UUID): Invoice? {
        return invoiceJpaRepository.findById(invoiceId)
            .map { invoiceMapper.toDomain(it) }
            .orElse(null)
    }
    
    override fun findByCode(clientId: UUID, invoiceCode: String): Invoice? {
        return invoiceJpaRepository.findByClientIdAndInvoiceCode(clientId, invoiceCode)
            ?.let { invoiceMapper.toDomain(it) }
    }
    
    override fun findByClient(clientId: UUID): List<Invoice> {
        return invoiceJpaRepository.findByClientId(clientId)
            .map { invoiceMapper.toDomain(it) }
    }
    
    override fun findByOrder(orderId: UUID): List<Invoice> {
        return invoiceJpaRepository.findByOrderId(orderId)
            .map { invoiceMapper.toDomain(it) }
    }
}
