package co.com.nauta.usecase

import co.com.nauta.model.bo.Invoice
import co.com.nauta.usecase.port.InvoicePort
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ProcessInvoiceUseCaseTest {

    private lateinit var processInvoiceUseCase: ProcessInvoiceUseCase
    private lateinit var mockInvoicePort: InvoicePort

    @BeforeEach
    fun setUp() {
        mockInvoicePort = createMockInvoicePort()
        processInvoiceUseCase = ProcessInvoiceUseCase(mockInvoicePort)
    }

    @Test
    fun `should create new invoice when invoice does not exist`() {
        // Given
        val invoiceCode = "IN123"
        val clientId = UUID.randomUUID()
        val orderId = UUID.randomUUID()

        // When
        val result = processInvoiceUseCase.process(invoiceCode, clientId, orderId)

        // Then
        assertNotNull(result)
        assertEquals(invoiceCode, result.invoiceCode)
        assertEquals(clientId, result.clientId)
        assertEquals(orderId, result.orderId)
        assertNotNull(result.createdAt)
        assertNotNull(result.updatedAt)
    }

    @Test
    fun `should update existing invoice when invoice exists`() {
        // Given
        val invoiceCode = "IN123"
        val clientId = UUID.randomUUID()
        val orderId = UUID.randomUUID()
        val existingInvoice = createInvoice(orderId, invoiceCode, clientId)
        
        // Mock existing invoice
        val mockPortWithExisting = object : InvoicePort {
            override fun save(invoice: Invoice): Invoice = invoice
            override fun findById(invoiceId: UUID): Invoice? = null
            override fun findByCode(clientId: UUID, invoiceCode: String): Invoice? = existingInvoice
            override fun findByOrder(orderId: UUID): List<Invoice> = listOf(existingInvoice)
            override fun findByClient(clientId: UUID): List<Invoice>  = emptyList()
        }
        
        val useCase = ProcessInvoiceUseCase(mockPortWithExisting)

        // When
        val result = useCase.process(invoiceCode, clientId, orderId)

        // Then
        assertNotNull(result)
        assertEquals(invoiceCode, result.invoiceCode)
        assertEquals(clientId, result.clientId)
        assertEquals(orderId, result.orderId)
        assertNotNull(result.updatedAt)
    }

    @Test
    fun `should process multiple invoices`() {
        // Given
        val invoiceCodes = listOf("IN123", "IN124", "IN125")
        val clientId = UUID.randomUUID()
        val orderId = UUID.randomUUID()

        // When
        val results = processInvoiceUseCase.processMultiple(invoiceCodes, clientId, orderId)

        // Then
        assertEquals(3, results.size)
        results.forEachIndexed { index, invoice ->
            assertEquals(invoiceCodes[index], invoice.invoiceCode)
            assertEquals(clientId, invoice.clientId)
            assertEquals(orderId, invoice.orderId)
        }
    }

    @Test
    fun `should handle empty invoice code`() {
        // Given
        val invoiceCode = ""
        val clientId = UUID.randomUUID()
        val orderId = UUID.randomUUID()

        // When
        val result = processInvoiceUseCase.process(invoiceCode, clientId, orderId)

        // Then
        assertNotNull(result)
        assertEquals(invoiceCode, result.invoiceCode)
        assertEquals(clientId, result.clientId)
        assertEquals(orderId, result.orderId)
    }

    @Test
    fun `should handle null order ID`() {
        // Given
        val invoiceCode = "IN123"
        val clientId = UUID.randomUUID()
        val orderId: UUID? = null

        // When
        val result = processInvoiceUseCase.process(invoiceCode, clientId, orderId)

        // Then
        assertNotNull(result)
        assertEquals(invoiceCode, result.invoiceCode)
        assertEquals(clientId, result.clientId)
        assertEquals(orderId, result.orderId)
    }

    @Test
    fun `should process empty list of invoices`() {
        // Given
        val invoiceCodes = emptyList<String>()
        val clientId = UUID.randomUUID()
        val orderId = UUID.randomUUID()

        // When
        val results = processInvoiceUseCase.processMultiple(invoiceCodes, clientId, orderId)

        // Then
        assertTrue(results.isEmpty())
    }

    private fun createInvoice(orderId: UUID, invoiceCode: String, clientId: UUID): Invoice {
        return Invoice().apply {
            this.clientId = clientId
            this.orderId = orderId
            this.invoiceCode = invoiceCode
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
        }
    }

    private fun createMockInvoicePort(): InvoicePort {
        return object : InvoicePort {
            override fun save(invoice: Invoice): Invoice = invoice
            override fun findById(invoiceId: UUID): Invoice? = null
            override fun findByCode(clientId: UUID, invoiceCode: String): Invoice? = null
            override fun findByOrder(orderId: UUID): List<Invoice> = emptyList()
            override fun findByClient(clientId: UUID): List<Invoice>  = emptyList()
        }
    }
}
