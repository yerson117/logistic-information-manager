package co.com.nauta.usecase

import co.com.nauta.model.bo.Booking
import co.com.nauta.model.bo.Container
import co.com.nauta.model.bo.Invoice
import co.com.nauta.model.bo.Order
import co.com.nauta.usecase.port.BookingPort
import co.com.nauta.usecase.port.ContainerPort
import co.com.nauta.usecase.port.InvoicePort
import co.com.nauta.usecase.port.OrderContainerPort
import co.com.nauta.usecase.port.OrderPort
import co.com.nauta.usecase.port.Page
import co.com.nauta.usecase.port.Pageable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ProcessEmailUseCaseTest {

    private lateinit var processEmailUseCase: ProcessEmailUseCase
    private lateinit var mockBookingPort: BookingPort
    private lateinit var mockContainerPort: ContainerPort
    private lateinit var mockOrderPort: OrderPort
    private lateinit var mockInvoicePort: InvoicePort
    private lateinit var mockOrderContainerPort: OrderContainerPort

    @BeforeEach
    fun setUp() {
        // Crear mocks simples sin MockK
        mockBookingPort = createMockBookingPort()
        mockContainerPort = createMockContainerPort()
        mockOrderPort = createMockOrderPort()
        mockInvoicePort = createMockInvoicePort()
        mockOrderContainerPort = createMockOrderContainerPort()

        // Crear casos de uso
        val processBookingUseCase = ProcessBookingUseCase(mockBookingPort)
        val processContainerUseCase = ProcessContainerUseCase(mockContainerPort, mockOrderContainerPort)
        val processOrderUseCase = ProcessOrderUseCase(mockOrderPort, mockOrderContainerPort)
        val processInvoiceUseCase = ProcessInvoiceUseCase(mockInvoicePort)

        processEmailUseCase = ProcessEmailUseCase(
            processBookingUseCase,
            processContainerUseCase,
            processOrderUseCase,
            processInvoiceUseCase,
            mockContainerPort,
            mockOrderPort
        )
    }

    @Test
    fun `should process email with booking and containers successfully`() {
        // Given
        val clientId = UUID.randomUUID()
        val booking = createBooking(clientId)
        val containers = listOf(createContainer(clientId))
        val orders = emptyList<Pair<Order, List<Invoice>>>()

        // When
        val result = processEmailUseCase.processEmail(booking, containers, orders)

        // Then
        assertNotNull(result)
        assertNotNull(result.booking)
        assertEquals(1, result.containers.size)
        assertEquals(0, result.orders.size)
    }

    @Test
    fun `should process email with orders and invoices successfully`() {
        // Given
        val clientId = UUID.randomUUID()
        val booking = null
        val containers = emptyList<Container>()
        val order = createOrder(clientId)
        val invoice = createInvoice(clientId, order.uuid)
        val orders = listOf(order to listOf(invoice))

        // When
        val result = processEmailUseCase.processEmail(booking, containers, orders)

        // Then
        assertNotNull(result)
        assertEquals(0, result.containers.size)
        assertEquals(1, result.orders.size)
        assertEquals(1, result.orders[0].second.size)
    }

    private fun createBooking(clientId: UUID): Booking {
        return Booking().apply {
            this.clientId = clientId
            bookingCode = "BK123"
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
        }
    }

    private fun createContainer(clientId: UUID): Container {
        return Container().apply {
            this.clientId = clientId
            containerCode = "MEDU1234567"
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
        }
    }

    private fun createOrder(clientId: UUID): Order {
        return Order().apply {
            this.clientId = clientId
            purchaseCode = "PO123"
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
        }
    }

    private fun createInvoice(clientId: UUID, orderId: UUID): Invoice {
        return Invoice().apply {
            this.clientId = clientId
            this.orderId = orderId
            invoiceCode = "IN123"
            amount = BigDecimal("1000.00")
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
        }
    }

    // Mocks simples
    private fun createMockBookingPort(): BookingPort {
        return object : BookingPort {
            override fun save(booking: Booking): Booking = booking
            override fun findById(bookingId: UUID): Booking? = null
            override fun findByCode(clientId: UUID, bookingCode: String): Booking? = null
            override fun findByClient(clientId: UUID): List<Booking> = emptyList()
        }
    }

    private fun createMockContainerPort(): ContainerPort {
        return object : ContainerPort {
            override fun save(container: Container): Container = container
            override fun findById(containerId: UUID): Container? = null
            override fun findByCode(clientId: UUID, containerCode: String): Container? = null
            override fun findByClient(clientId: UUID): List<Container> = emptyList()
            override fun findByClient(clientId: UUID, pageable: Pageable): Page<Container> {
                return object : Page<Container> {
                    override val content: List<Container> = emptyList()
                    override val page: Int = 0
                    override val size: Int = 20
                    override val totalElements: Long = 0
                    override val totalPages: Int = 0
                    override val isFirst: Boolean = true
                    override val isLast: Boolean = true
                    override val numberOfElements: Int = 0
                }
            }

            override fun findByBooking(bookingId: UUID): List<Container> = emptyList()
            override fun findByOrder(orderId: UUID): List<Container> = emptyList()
            override fun findByOrder(
                orderId: UUID,
                pageable: Pageable
            ): Page<Container> {
                return object : Page<Container> {
                    override val content: List<Container> = emptyList()
                    override val page: Int = 0
                    override val size: Int = 20
                    override val totalElements: Long = 0
                    override val totalPages: Int = 0
                    override val isFirst: Boolean = true
                    override val isLast: Boolean = true
                    override val numberOfElements: Int = 0
                }}
        }
    }

    private fun createMockOrderPort(): OrderPort {
        return object : OrderPort {
            override fun save(order: Order): Order = order
            override fun findById(orderId: UUID): Order? = null
            override fun findByPurchaseCode(clientId: UUID, purchaseCode: String): Order? = null
            override fun findByClient(clientId: UUID): List<Order> = emptyList()
            override fun findByClient(clientId: UUID, pageable: Pageable): Page<Order> {
                return object : Page<Order> {
                    override val content: List<Order> = emptyList()
                    override val page: Int = 0
                    override val size: Int = 20
                    override val totalElements: Long = 0
                    override val totalPages: Int = 0
                    override val isFirst: Boolean = true
                    override val isLast: Boolean = true
                    override val numberOfElements: Int = 0
                }
            }
            override fun findByBooking(bookingId: UUID): List<Order> = emptyList()
            override fun findByContainer(containerId: UUID): List<Order> = emptyList()
            override fun findByContainer(
                containerId: UUID,
                pageable: Pageable
            ): Page<Order> {
                return object : Page<Order> {
                    override val content: List<Order> = emptyList()
                    override val page: Int = 0
                    override val size: Int = 20
                    override val totalElements: Long = 0
                    override val totalPages: Int = 0
                    override val isFirst: Boolean = true
                    override val isLast: Boolean = true
                    override val numberOfElements: Int = 0
                }}
        }
    }

    private fun createMockInvoicePort(): InvoicePort {
        return object : InvoicePort {
            override fun save(invoice: Invoice): Invoice = invoice
            override fun findById(invoiceId: UUID): Invoice? = null
            override fun findByCode(clientId: UUID, invoiceCode: String): Invoice? = null
            override fun findByClient(clientId: UUID): List<Invoice> = emptyList()
            override fun findByOrder(orderId: UUID): List<Invoice> = emptyList()
        }
    }

    private fun createMockOrderContainerPort(): OrderContainerPort {
        return object : OrderContainerPort {
            override fun save(orderContainer: co.com.nauta.model.bo.OrderContainer): co.com.nauta.model.bo.OrderContainer = orderContainer
            override fun findByOrderAndContainer(orderId: UUID, containerId: UUID): co.com.nauta.model.bo.OrderContainer? = null
            override fun findByOrder(orderId: UUID): List<co.com.nauta.model.bo.OrderContainer> = emptyList()
            override fun findByContainer(containerId: UUID): List<co.com.nauta.model.bo.OrderContainer> = emptyList()
            override fun deleteOrderContainer(orderId: UUID, containerId: UUID) {}
        }
    }
}
