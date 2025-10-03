package co.com.nauta.usecase

import co.com.nauta.model.bo.Order
import co.com.nauta.model.bo.OrderContainer
import co.com.nauta.usecase.port.OrderPort
import co.com.nauta.usecase.port.OrderContainerPort
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ProcessOrderUseCaseTest {

    private lateinit var processOrderUseCase: ProcessOrderUseCase
    private lateinit var mockOrderPort: OrderPort
    private lateinit var mockOrderContainerPort: OrderContainerPort

    @BeforeEach
    fun setUp() {
        mockOrderPort = createMockOrderPort()
        mockOrderContainerPort = createMockOrderContainerPort()
        processOrderUseCase = ProcessOrderUseCase(mockOrderPort, mockOrderContainerPort)
    }

    @Test
    fun `should create new order when order does not exist`() {
        // Given
        val purchaseCode = "PO123"
        val clientId = UUID.randomUUID()
        val bookingId = UUID.randomUUID()

        // When
        val result = processOrderUseCase.process(purchaseCode, clientId, bookingId)

        // Then
        assertNotNull(result)
        assertEquals(purchaseCode, result.purchaseCode)
        assertEquals(clientId, result.clientId)
        assertEquals(bookingId, result.bookingId)
        assertNotNull(result.createdAt)
        assertNotNull(result.updatedAt)
    }

    @Test
    fun `should update existing order when order exists`() {
        // Given
        val purchaseCode = "PO123"
        val clientId = UUID.randomUUID()
        val bookingId = UUID.randomUUID()
        val existingOrder = createOrder(clientId, purchaseCode)
        
        // Mock existing order
        val mockPortWithExisting = object : OrderPort {
            override fun save(order: Order): Order = order
            override fun findById(orderId: UUID): Order? = null
            override fun findByPurchaseCode(clientId: UUID, purchaseCode: String): Order? = existingOrder
            override fun findByClient(clientId: UUID): List<Order> = listOf(existingOrder)
            override fun findByClient(clientId: UUID, pageable: co.com.nauta.usecase.port.Pageable): co.com.nauta.usecase.port.Page<Order> {
                return object : co.com.nauta.usecase.port.Page<Order> {
                    override val content: List<Order> = listOf(existingOrder)
                    override val page: Int = 0
                    override val size: Int = 20
                    override val totalElements: Long = 1
                    override val totalPages: Int = 1
                    override val isFirst: Boolean = true
                    override val isLast: Boolean = true
                    override val numberOfElements: Int = 1
                }
            }
            override fun findByBooking(bookingId: UUID): List<Order> = listOf(existingOrder)
            override fun findByContainer(containerId: UUID): List<Order> = emptyList()
            override fun findByContainer(containerId: UUID, pageable: co.com.nauta.usecase.port.Pageable): co.com.nauta.usecase.port.Page<Order> {
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
        
        val useCase = ProcessOrderUseCase(mockPortWithExisting, mockOrderContainerPort)

        // When
        val result = useCase.process(purchaseCode, clientId, bookingId)

        // Then
        assertNotNull(result)
        assertEquals(purchaseCode, result.purchaseCode)
        assertEquals(clientId, result.clientId)
        assertEquals(bookingId, result.bookingId)
        assertNotNull(result.updatedAt)
    }

    @Test
    fun `should process multiple orders`() {
        // Given
        val purchaseCodes = listOf("PO123", "PO124", "PO125")
        val clientId = UUID.randomUUID()
        val bookingId = UUID.randomUUID()

        // When
        val results = processOrderUseCase.processMultiple(purchaseCodes, clientId, bookingId)

        // Then
        assertEquals(3, results.size)
        results.forEachIndexed { index, order ->
            assertEquals(purchaseCodes[index], order.purchaseCode)
            assertEquals(clientId, order.clientId)
            assertEquals(bookingId, order.bookingId)
        }
    }

    @Test
    fun `should associate order with containers`() {
        // Given
        val orderId = UUID.randomUUID()
        val containerIds = listOf(UUID.randomUUID(), UUID.randomUUID())

        // When
        processOrderUseCase.associateWithContainers(orderId, containerIds)

        // Then - No exception should be thrown
        assertTrue(true)
    }

    @Test
    fun `should get associated containers for order`() {
        // Given
        val orderId = UUID.randomUUID()

        // When
        val result = processOrderUseCase.getAssociatedContainers(orderId)

        // Then
        assertNotNull(result)
        assertTrue(result.isEmpty())
    }

    private fun createOrder(clientId: UUID, purchaseCode: String): Order {
        return Order().apply {
            this.clientId = clientId
            this.purchaseCode = purchaseCode
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
        }
    }

    private fun createMockOrderPort(): OrderPort {
        return object : OrderPort {
            override fun save(order: Order): Order = order
            override fun findById(orderId: UUID): Order? = null
            override fun findByPurchaseCode(clientId: UUID, purchaseCode: String): Order? = null
            override fun findByClient(clientId: UUID): List<Order> = emptyList()
            override fun findByClient(clientId: UUID, pageable: co.com.nauta.usecase.port.Pageable): co.com.nauta.usecase.port.Page<Order> {
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
            override fun findByContainer(containerId: UUID, pageable: co.com.nauta.usecase.port.Pageable): co.com.nauta.usecase.port.Page<Order> {
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

    private fun createMockOrderContainerPort(): OrderContainerPort {
        return object : OrderContainerPort {
            override fun save(orderContainer: OrderContainer): OrderContainer = orderContainer
            override fun findByOrderAndContainer(orderId: UUID, containerId: UUID): OrderContainer? = null
            override fun findByOrder(orderId: UUID): List<OrderContainer> = emptyList()
            override fun findByContainer(containerId: UUID): List<OrderContainer> = emptyList()
            override fun deleteOrderContainer(orderId: UUID, containerId: UUID) {}
        }
    }
}
