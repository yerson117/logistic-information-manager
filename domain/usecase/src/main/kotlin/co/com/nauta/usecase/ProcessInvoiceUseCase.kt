package co.com.nauta.usecase

import co.com.nauta.model.bo.Invoice
import co.com.nauta.usecase.port.InvoicePort
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class ProcessInvoiceUseCase(
    private val invoicePort: InvoicePort
) {
    
    fun process(invoiceCode: String, clientId: UUID, orderId: UUID? = null, amount: BigDecimal? = null): Invoice {
        val existingInvoice = invoicePort.findByCode(clientId, invoiceCode)
        
        return if (existingInvoice != null) {
            existingInvoice.orderId = orderId
            if (amount != null) {
                existingInvoice.amount = amount
            }
            existingInvoice.updatedAt = LocalDateTime.now()
            invoicePort.save(existingInvoice)
        } else {
            val newInvoice = Invoice()
            newInvoice.invoiceCode = invoiceCode
            newInvoice.clientId = clientId
            newInvoice.orderId = orderId
            newInvoice.amount = amount ?: BigDecimal.ZERO
            newInvoice.createdAt = LocalDateTime.now()
            newInvoice.updatedAt = LocalDateTime.now()
            invoicePort.save(newInvoice)
        }
    }
    
    fun processMultiple(invoiceCodes: List<String>, clientId: UUID, orderId: UUID? = null): List<Invoice> {
        return invoiceCodes.map { invoiceCode ->
            process(invoiceCode, clientId, orderId)
        }
    }
}
