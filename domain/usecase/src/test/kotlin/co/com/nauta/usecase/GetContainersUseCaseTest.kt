package co.com.nauta.usecase

import co.com.nauta.model.bo.Container
import co.com.nauta.model.bo.Order
import co.com.nauta.usecase.port.ContainerPort
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

class GetContainersUseCaseTest {

    private lateinit var getContainersUseCase: GetContainersUseCase
    private lateinit var mockContainerPort: ContainerPort
    private lateinit var mockOrderPort: OrderPort

    @BeforeEach
    fun setUp() {
        mockContainerPort = createMockContainerPort()
        mockOrderPort = createMockOrderPort()
        getContainersUseCase = GetContainersUseCase(mockContainerPort, mockOrderPort)
    }

    @Test
    fun `should get containers by client without pagination`() {
        // Given
        val clientId = UUID.randomUUID()
        val containers = listOf(
            createContainer(clientId, "MEDU1234567"),
            createContainer(clientId, "MEDU1234568")
        )
        
        val mockPortWithData = object : ContainerPort {
            override fun save(container: Container): Container = container
            override fun findById(containerId: UUID): Container? = null
            override fun findByCode(clientId: UUID, containerCode: String): Container? = null
            override fun findByClient(clientId: UUID): List<Container> = containers
            override fun findByClient(clientId: UUID, pageable: Pageable): co.com.nauta.usecase.port.Page<Container> {
                return object : co.com.nauta.usecase.port.Page<Container> {
                    override val content: List<Container> = containers
                    override val page: Int = 0
                    override val size: Int = 20
                    override val totalElements: Long = 2
                    override val totalPages: Int = 1
                    override val isFirst: Boolean = true
                    override val isLast: Boolean = true
                    override val numberOfElements: Int = 2
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
        
        val useCase = GetContainersUseCase(mockPortWithData, mockOrderPort)

        // When
        val result = useCase.getContainersByClient(clientId)

        // Then
        assertEquals(2, result.size)
        assertEquals("MEDU1234567", result[0].containerCode)
        assertEquals("MEDU1234568", result[1].containerCode)
    }

    @Test
    fun `should get containers by client with pagination`() {
        // Given
        val clientId = UUID.randomUUID()
        val pageable = createPageable(0, 10)
        val containers = listOf(createContainer(clientId, "MEDU1234567"))
        
        val mockPortWithData = object : ContainerPort {
            override fun save(container: Container): Container = container
            override fun findById(containerId: UUID): Container? = null
            override fun findByCode(clientId: UUID, containerCode: String): Container? = null
            override fun findByClient(clientId: UUID): List<Container> = containers
            override fun findByClient(clientId: UUID, pageable: Pageable): co.com.nauta.usecase.port.Page<Container> {
                return object : co.com.nauta.usecase.port.Page<Container> {
                    override val content: List<Container> = containers
                    override val page: Int = 0
                    override val size: Int = 10
                    override val totalElements: Long = 1
                    override val totalPages: Int = 1
                    override val isFirst: Boolean = true
                    override val isLast: Boolean = true
                    override val numberOfElements: Int = 1
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
        
        val useCase = GetContainersUseCase(mockPortWithData, mockOrderPort)

        // When
        val result = useCase.getContainersByClient(clientId, pageable)

        // Then
        assertEquals(1, result.content.size)
        assertEquals(0, result.page)
        assertEquals(10, result.size)
        assertEquals(1, result.totalElements)
    }

    @Test
    fun `should get container by id`() {
        // Given
        val containerId = UUID.randomUUID()
        val clientId = UUID.randomUUID()
        val container = createContainer(clientId, "MEDU1234567")
        
        val mockPortWithData = object : ContainerPort {
            override fun save(container: Container): Container = container
            override fun findById(containerId: UUID): Container? = if (containerId == containerId) container else null
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
        
        val useCase = GetContainersUseCase(mockPortWithData, mockOrderPort)

        // When
        val result = useCase.getContainerById(containerId)

        // Then
        assertNotNull(result)
        assertEquals("MEDU1234567", result.containerCode)
    }

    @Test
    fun `should return null when container not found by id`() {
        // Given
        val containerId = UUID.randomUUID()

        // When
        val result = getContainersUseCase.getContainerById(containerId)

        // Then
        assertNull(result)
    }

    @Test
    fun `should get orders by container with pagination`() {
        // Given
        val containerId = UUID.randomUUID()
        val pageable = createPageable(0, 5)
        val orders = listOf(createOrder(UUID.randomUUID(), "PO123"))
        
        val mockOrderPortWithData = object : OrderPort {
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
            override fun findByContainer(containerId: UUID): List<Order> = orders
            override fun findByContainer(containerId: UUID, pageable: Pageable): co.com.nauta.usecase.port.Page<Order> {
                return object : co.com.nauta.usecase.port.Page<Order> {
                    override val content: List<Order> = orders
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
        
        val useCase = GetContainersUseCase(mockContainerPort, mockOrderPortWithData)

        // When
        val result = useCase.getOrdersByContainer(containerId, pageable)

        // Then
        assertEquals(1, result.content.size)
        assertEquals("PO123", result.content[0].purchaseCode)
        assertEquals(0, result.page)
        assertEquals(5, result.size)
    }

    private fun createContainer(clientId: UUID, containerCode: String): Container {
        return Container().apply {
            this.clientId = clientId
            this.containerCode = containerCode
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
        }
    }

    private fun createOrder(clientId: UUID, purchaseCode: String): Order {
        return Order().apply {
            this.clientId = clientId
            this.purchaseCode = purchaseCode
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
}
