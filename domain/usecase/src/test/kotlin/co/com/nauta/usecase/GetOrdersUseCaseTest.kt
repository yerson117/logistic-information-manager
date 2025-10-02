package co.com.nauta.usecase

import co.com.nauta.model.bo.Container
import co.com.nauta.model.bo.Invoice
import co.com.nauta.model.bo.Order
import co.com.nauta.usecase.port.ContainerPort
import co.com.nauta.usecase.port.InvoicePort
import co.com.nauta.usecase.port.OrderPort
import co.com.nauta.usecase.port.Pageable
import co.com.nauta.usecase.port.Sort
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GetOrdersUseCaseTest {

    private lateinit var getOrdersUseCase: GetOrdersUseCase
    private lateinit var mockOrderPort: OrderPort
    private lateinit var mockContainerPort: ContainerPort
    private lateinit var mockInvoicePort: InvoicePort

    @BeforeEach
    fun setUp() {
        mockOrderPort = createMockOrderPort()
        mockContainerPort = createMockContainerPort()
        mockInvoicePort = createMockInvoicePort()
        getOrdersUseCase = GetOrdersUseCase(mockOrderPort, mockContainerPort, mockInvoicePort)
    }

    @Test
    fun `should get orders by client without pagination`() {
        // Given
        val clientId = UUID.randomUUID()
        val orders = listOf(
            createOrder(clientId, "PO123"),
            createOrder(clientId, "PO124")
        )
        
        val mockPortWithData = object : OrderPort {
            override fun save(order: Order): Order = order
            override fun findById(orderId: UUID): Order? = null
            override fun findByPurchaseCode(clientId: UUID, purchaseCode: String): Order? = null
            override fun findByClient(clientId: UUID): List<Order> = orders
            override fun findByClient(clientId: UUID, pageable: Pageable): co.com.nauta.usecase.port.Page<Order> {
                return object : co.com.nauta.usecase.port.Page<Order> {
                    override val content: List<Order> = orders
                    override val page: Int = 0
                    override val size: Int = 20
                    override val totalElements: Long = 2
                    override val totalPages: Int = 1
                    override val isFirst: Boolean = true
                    override val isLast: Boolean = true
                    override val numberOfElements: Int = 2
                }
            }
            override fun findByBooking(bookingId: UUID): List<Order> = emptyList()
            override fun findByContainer(containerId: UUID): List<Order> = emptyList()
            override fun findByContainer(containerId: UUID, pageable: Pageable): co.com.nauta.usecase.port.Page<Order> {
                return object : co.com.nauta.usecase.port.Page<Order> {
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
        }
        
        val useCase = GetOrdersUseCase(mockPortWithData, mockContainerPort, mockInvoicePort)

        // When
        val result = useCase.getOrdersByClient(clientId)

        // Then
        assertEquals(2, result.size)
        assertEquals("PO123", result[0].purchaseCode)
        assertEquals("PO124", result[1].purchaseCode)
    }

    @Test
    fun `should get orders by client with pagination`() {
        // Given
        val clientId = UUID.randomUUID()
        val pageable = createPageable(0, 10)
        val orders = listOf(createOrder(clientId, "PO123"))
        
        val mockPortWithData = object : OrderPort {
            override fun save(order: Order): Order = order
            override fun findById(orderId: UUID): Order? = null
            override fun findByPurchaseCode(clientId: UUID, purchaseCode: String): Order? = null
            override fun findByClient(clientId: UUID): List<Order> = orders
            override fun findByClient(clientId: UUID, pageable: Pageable): co.com.nauta.usecase.port.Page<Order> {
                return object : co.com.nauta.usecase.port.Page<Order> {
                    override val content: List<Order> = orders
                    override val page: Int = 0
                    override val size: Int = 10
                    override val totalElements: Long = 1
                    override val totalPages: Int = 1
                    override val isFirst: Boolean = true
                    override val isLast: Boolean = true
                    override val numberOfElements: Int = 1
                }
            }
            override fun findByBooking(bookingId: UUID): List<Order> = emptyList()
            override fun findByContainer(containerId: UUID): List<Order> = emptyList()
            override fun findByContainer(containerId: UUID, pageable: Pageable): co.com.nauta.usecase.port.Page<Order> {
                return object : co.com.nauta.usecase.port.Page<Order> {
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
        }
        
        val useCase = GetOrdersUseCase(mockPortWithData, mockContainerPort, mockInvoicePort)

        // When
        val result = useCase.getOrdersByClient(clientId, pageable)

        // Then
        assertEquals(1, result.content.size)
        assertEquals(0, result.page)
        assertEquals(10, result.size)
        assertEquals(1, result.totalElements)
    }

    @Test
    fun `should get order by id`() {
        // Given
        val orderId = UUID.randomUUID()
        val clientId = UUID.randomUUID()
        val order = createOrder(clientId, "PO123")
        
        val mockPortWithData = object : OrderPort {
            override fun save(order: Order): Order = order
            override fun findById(orderId: UUID): Order? = if (orderId == orderId) order else null
            override fun findByPurchaseCode(clientId: UUID, purchaseCode: String): Order? = null
            override fun findByClient(clientId: UUID): List<Order> = emptyList()
            override fun findByClient(clientId: UUID, pageable: Pageable): co.com.nauta.usecase.port.Page<Order> {
                return object : co.com.nauta.usecase.port.Page<Order> {
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
            override fun findByContainer(containerId: UUID, pageable: Pageable): co.com.nauta.usecase.port.Page<Order> {
                return object : co.com.nauta.usecase.port.Page<Order> {
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
        }
        
        val useCase = GetOrdersUseCase(mockPortWithData, mockContainerPort, mockInvoicePort)

        // When
        val result = useCase.getOrderById(orderId)

        // Then
        assertNotNull(result)
        assertEquals("PO123", result.purchaseCode)
    }

    @Test
    fun `should return null when order not found by id`() {
        // Given
        val orderId = UUID.randomUUID()

        // When
        val result = getOrdersUseCase.getOrderById(orderId)

        // Then
        assertNull(result)
    }

    @Test
    fun `should get containers by order with pagination`() {
        // Given
        val orderId = UUID.randomUUID()
        val pageable = createPageable(0, 5)
        val containers = listOf(createContainer(UUID.randomUUID(), "MEDU1234567"))
        
        val mockContainerPortWithData = object : ContainerPort {
            override fun save(container: Container): Container = container
            override fun findById(containerId: UUID): Container? = null
            override fun findByCode(clientId: UUID, containerCode: String): Container? = null
            override fun findByClient(clientId: UUID): List<Container> = emptyList()
            override fun findByClient(clientId: UUID, pageable: Pageable): co.com.nauta.usecase.port.Page<Container> {
                return object : co.com.nauta.usecase.port.Page<Container> {
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
            override fun findByOrder(orderId: UUID): List<Container> = containers
            override fun findByOrder(orderId: UUID, pageable: Pageable): co.com.nauta.usecase.port.Page<Container> {
                return object : co.com.nauta.usecase.port.Page<Container> {
                    override val content: List<Container> = containers
                    override val page: Int = 0
                    override val size: Int = 5
                    override val totalElements: Long = 1
                    override val totalPages: Int = 1
                    override val isFirst: Boolean = true
                    override val isLast: Boolean = true
                    override val numberOfElements: Int = 1
                }
            }
        }
        
        val useCase = GetOrdersUseCase(mockOrderPort, mockContainerPortWithData, mockInvoicePort)

        // When
        val result = useCase.getContainersByOrder(orderId, pageable)

        // Then
        assertEquals(1, result.content.size)
        assertEquals("MEDU1234567", result.content[0].containerCode)
        assertEquals(0, result.page)
        assertEquals(5, result.size)
    }

    @Test
    fun `should get invoices by order`() {
        // Given
        val orderId = UUID.randomUUID()
        val invoices = listOf(
            createInvoice(orderId, "IN123"),
            createInvoice(orderId, "IN124")
        )
        
        val mockInvoicePortWithData = object : InvoicePort {
            override fun save(invoice: Invoice): Invoice = invoice
            override fun findById(invoiceId: UUID): Invoice? = null
            override fun findByCode(clientId: UUID, invoiceCode: String): Invoice? = null
            override fun findByClient(clientId: UUID): List<Invoice>  = emptyList()

            override fun findByOrder(orderId: UUID): List<Invoice> = invoices
        }
        
        val useCase = GetOrdersUseCase(mockOrderPort, mockContainerPort, mockInvoicePortWithData)

        // When
        val result = useCase.getInvoicesByOrder(orderId)

        // Then
        assertEquals(2, result.size)
        assertEquals("IN123", result[0].invoiceCode)
        assertEquals("IN124", result[1].invoiceCode)
    }

    private fun createOrder(clientId: UUID, purchaseCode: String): Order {
        return Order().apply {
            this.clientId = clientId
            this.purchaseCode = purchaseCode
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
        }
    }

    private fun createContainer(clientId: UUID, containerCode: String): Container {
        return Container().apply {
            this.clientId = clientId
            this.containerCode = containerCode
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
        }
    }

    private fun createInvoice(orderId: UUID, invoiceCode: String): Invoice {
        return Invoice().apply {
            this.orderId = orderId
            this.invoiceCode = invoiceCode
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
        }
    }

    private fun createPageable(page: Int, size: Int): Pageable {
        return object : Pageable {
            override val page: Int = page
            override val size: Int = size
            override val sort: Sort? = null
        }
    }

    private fun createMockOrderPort(): OrderPort {
        return object : OrderPort {
            override fun save(order: Order): Order = order
            override fun findById(orderId: UUID): Order? = null
            override fun findByPurchaseCode(clientId: UUID, purchaseCode: String): Order? = null
            override fun findByClient(clientId: UUID): List<Order> = emptyList()
            override fun findByClient(clientId: UUID, pageable: Pageable): co.com.nauta.usecase.port.Page<Order> {
                return object : co.com.nauta.usecase.port.Page<Order> {
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
            override fun findByContainer(containerId: UUID, pageable: Pageable): co.com.nauta.usecase.port.Page<Order> {
                return object : co.com.nauta.usecase.port.Page<Order> {
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
        }
    }

    private fun createMockContainerPort(): ContainerPort {
        return object : ContainerPort {
            override fun save(container: Container): Container = container
            override fun findById(containerId: UUID): Container? = null
            override fun findByCode(clientId: UUID, containerCode: String): Container? = null
            override fun findByClient(clientId: UUID): List<Container> = emptyList()
            override fun findByClient(clientId: UUID, pageable: Pageable): co.com.nauta.usecase.port.Page<Container> {
                return object : co.com.nauta.usecase.port.Page<Container> {
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
            override fun findByOrder(orderId: UUID, pageable: Pageable): co.com.nauta.usecase.port.Page<Container> {
                return object : co.com.nauta.usecase.port.Page<Container> {
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
