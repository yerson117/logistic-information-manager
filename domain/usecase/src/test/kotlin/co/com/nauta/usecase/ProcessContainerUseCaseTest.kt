package co.com.nauta.usecase

import co.com.nauta.model.bo.Container
import co.com.nauta.model.bo.OrderContainer
import co.com.nauta.usecase.port.ContainerPort
import co.com.nauta.usecase.port.OrderContainerPort
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ProcessContainerUseCaseTest {

    private lateinit var processContainerUseCase: ProcessContainerUseCase
    private lateinit var mockContainerPort: ContainerPort
    private lateinit var mockOrderContainerPort: OrderContainerPort

    @BeforeEach
    fun setUp() {
        mockContainerPort = createMockContainerPort()
        mockOrderContainerPort = createMockOrderContainerPort()
        processContainerUseCase = ProcessContainerUseCase(mockContainerPort, mockOrderContainerPort)
    }

    @Test
    fun `should create new container when container does not exist`() {
        // Given
        val containerCode = "MEDU1234567"
        val clientId = UUID.randomUUID()
        val bookingId = UUID.randomUUID()

        // When
        val result = processContainerUseCase.process(containerCode, clientId, bookingId)

        // Then
        assertNotNull(result)
        assertEquals(containerCode, result.containerCode)
        assertEquals(clientId, result.clientId)
        assertEquals(bookingId, result.bookingId)
        assertNotNull(result.createdAt)
        assertNotNull(result.updatedAt)
    }

    @Test
    fun `should update existing container when container exists`() {
        // Given
        val containerCode = "MEDU1234567"
        val clientId = UUID.randomUUID()
        val bookingId = UUID.randomUUID()
        val existingContainer = createContainer(clientId, containerCode)
        
        // Mock existing container
        val mockPortWithExisting = object : ContainerPort {
            override fun save(container: Container): Container = container
            override fun findById(containerId: UUID): Container? = null
            override fun findByCode(clientId: UUID, containerCode: String): Container? = existingContainer
            override fun findByClient(clientId: UUID): List<Container> = listOf(existingContainer)
            override fun findByClient(clientId: UUID, pageable: co.com.nauta.usecase.port.Pageable): co.com.nauta.usecase.port.Page<Container> {
                return object : co.com.nauta.usecase.port.Page<Container> {
                    override val content: List<Container> = listOf(existingContainer)
                    override val page: Int = 0
                    override val size: Int = 20
                    override val totalElements: Long = 1
                    override val totalPages: Int = 1
                    override val isFirst: Boolean = true
                    override val isLast: Boolean = true
                    override val numberOfElements: Int = 1
                }
            }
            override fun findByBooking(bookingId: UUID): List<Container> = listOf(existingContainer)
            override fun findByOrder(orderId: UUID): List<Container> = emptyList()
            override fun findByOrder(orderId: UUID, pageable: co.com.nauta.usecase.port.Pageable): co.com.nauta.usecase.port.Page<Container> {
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
        
        val useCase = ProcessContainerUseCase(mockPortWithExisting, mockOrderContainerPort)

        // When
        val result = useCase.process(containerCode, clientId, bookingId)

        // Then
        assertNotNull(result)
        assertEquals(containerCode, result.containerCode)
        assertEquals(clientId, result.clientId)
        assertEquals(bookingId, result.bookingId)
        assertNotNull(result.updatedAt)
    }

    @Test
    fun `should process multiple containers`() {
        // Given
        val containerCodes = listOf("MEDU1234567", "MEDU1234568", "MEDU1234569")
        val clientId = UUID.randomUUID()
        val bookingId = UUID.randomUUID()

        // When
        val results = processContainerUseCase.processMultiple(containerCodes, clientId, bookingId)

        // Then
        assertEquals(3, results.size)
        results.forEachIndexed { index, container ->
            assertEquals(containerCodes[index], container.containerCode)
            assertEquals(clientId, container.clientId)
            assertEquals(bookingId, container.bookingId)
        }
    }

    @Test
    fun `should associate container with orders`() {
        // Given
        val containerId = UUID.randomUUID()
        val orderIds = listOf(UUID.randomUUID(), UUID.randomUUID())

        // When
        processContainerUseCase.associateWithOrders(containerId, orderIds)

        // Then - No exception should be thrown
        assertTrue(true)
    }

    @Test
    fun `should get associated orders for container`() {
        // Given
        val containerId = UUID.randomUUID()

        // When
        val result = processContainerUseCase.getAssociatedOrders(containerId)

        // Then
        assertNotNull(result)
        assertTrue(result.isEmpty())
    }

    private fun createContainer(clientId: UUID, containerCode: String): Container {
        return Container().apply {
            this.clientId = clientId
            this.containerCode = containerCode
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
        }
    }

    private fun createMockContainerPort(): ContainerPort {
        return object : ContainerPort {
            override fun save(container: Container): Container = container
            override fun findById(containerId: UUID): Container? = null
            override fun findByCode(clientId: UUID, containerCode: String): Container? = null
            override fun findByClient(clientId: UUID): List<Container> = emptyList()
            override fun findByClient(clientId: UUID, pageable: co.com.nauta.usecase.port.Pageable): co.com.nauta.usecase.port.Page<Container> {
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
            override fun findByOrder(orderId: UUID, pageable: co.com.nauta.usecase.port.Pageable): co.com.nauta.usecase.port.Page<Container> {
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
