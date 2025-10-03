package co.com.nauta.model.bo

import java.math.BigDecimal
import java.util.UUID

/**
 * INVOICE entity representing an invoice in the logistics system
 * 
 * Attributes:
 * - invoice_id: UUID, Primary Key
 * - client_id: UUID, Foreign Key to CLIENT
 * - order_id: UUID, Foreign Key to ORDER (nullable)
 * - invoice_code: String, unique invoice code (e.g., "IN123")
 * - amount: BigDecimal, invoice amount
 * - created_at: LocalDateTime, creation timestamp
 * - updated_at: LocalDateTime, last update timestamp
 * 
 * Relationships:
 * - CLIENT to INVOICE: A CLIENT can have multiple invoices
 * - ORDER to INVOICE: "puede generar" (can generate). An ORDER can generate zero or many INVOICEs
 */
class Invoice : BaseModelBO() {
    var clientId: UUID? = null
    var orderId: UUID? = null
    var invoiceCode: String? = null
    var amount: BigDecimal? = null
}
