package co.com.nauta.usecase.port

import co.com.nauta.model.bo.Invoice
import java.util.UUID

/**
 * Port for Invoice operations
 */
interface InvoicePort {
    fun save(invoice: Invoice): Invoice
    fun findById(invoiceId: UUID): Invoice?
    fun findByCode(clientId: UUID, invoiceCode: String): Invoice?
    fun findByClient(clientId: UUID): List<Invoice>
    fun findByOrder(orderId: UUID): List<Invoice>
}
