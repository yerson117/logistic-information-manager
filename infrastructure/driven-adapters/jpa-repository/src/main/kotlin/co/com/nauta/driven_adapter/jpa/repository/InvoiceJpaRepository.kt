package co.com.nauta.driven_adapter.jpa.repository

import co.com.nauta.driven_adapter.jpa.entity.InvoiceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * JPA Repository for InvoiceEntity
 * Following hexagonal architecture principles - one repository per entity
 */
@Repository
interface InvoiceJpaRepository : JpaRepository<InvoiceEntity, UUID> {
    fun findByClientIdAndInvoiceCode(clientId: UUID, invoiceCode: String): InvoiceEntity?
    fun findByClientId(clientId: UUID): List<InvoiceEntity>
    fun findByOrderId(orderId: UUID): List<InvoiceEntity>
}
