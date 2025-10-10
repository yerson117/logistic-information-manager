package co.com.nauta.driven_adapter.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

/**
 * JPA Entity for Invoice
 * This represents the database table structure for Invoice domain entity
 */
@Entity
@Table(name = "invoices")
data class InvoiceEntity(
    @Id
    @Column(name = "invoice_id")
    val invoiceId: UUID,
    
    @Column(name = "client_id", nullable = false)
    val clientId: UUID,
    
    @Column(name = "order_id")
    val orderId: UUID?,
    
    @Column(name = "invoice_code", nullable = false)
    val invoiceCode: String,
    
    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    val amount: BigDecimal,
    
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,
    
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime
) {
    constructor() : this(UUID.randomUUID(), UUID.randomUUID(), null, "", BigDecimal.ZERO, LocalDateTime.now(), LocalDateTime.now())
}
