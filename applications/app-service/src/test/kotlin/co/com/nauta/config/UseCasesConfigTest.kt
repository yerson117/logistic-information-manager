package co.com.nauta.config

import co.com.nauta.usecase.port.ContainerPort
import co.com.nauta.usecase.port.OrderPort
import co.com.nauta.usecase.port.Page
import co.com.nauta.usecase.port.Pageable
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import co.com.nauta.model.bo.Booking
import co.com.nauta.model.bo.Container
import co.com.nauta.model.bo.Invoice
import co.com.nauta.model.bo.Order
import co.com.nauta.model.bo.OrderContainer
import java.util.UUID
import kotlin.test.assertTrue

class UseCasesConfigTest {

    @Test
    fun testUseCaseBeansExist() {
        AnnotationConfigApplicationContext(TestConfig::class.java).use { context ->
            val beanNames = context.beanDefinitionNames

            // Verificar que los beans de casos de uso existen
            var useCaseBeanFound = false
            for (beanName in beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true
                    break
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'UseCase' were found")
        }
    }

    @Configuration
    @Import(UseCasesConfig::class)
    class TestConfig {

        @Bean
        @Primary
        fun bookingPort(): co.com.nauta.usecase.port.BookingPort {
            return object : co.com.nauta.usecase.port.BookingPort {
                override fun save(booking: Booking) = booking
                override fun findById(bookingId: UUID) = null
                override fun findByCode(clientId: UUID, bookingCode: String) = null
                override fun findByClient(clientId: UUID) = emptyList<Booking>()
            }
        }

        @Bean
        @Primary
        fun containerPort(): ContainerPort {
            return object : ContainerPort {
                override fun save(container: Container) = container
                override fun findById(containerId: UUID) = null
                override fun findByCode(clientId: UUID, containerCode: String) = null
                override fun findByClient(clientId: UUID) = emptyList<Container>()
                override fun findByClient(
                    clientId: UUID,
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
                    }
                }

                override fun findByBooking(bookingId: UUID) = emptyList<Container>()
                override fun findByOrder(orderId: UUID) = emptyList<Container>()
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
                    }
                }
            }
        }

        @Bean
        @Primary
        fun orderPort(): OrderPort {
            return object : OrderPort {
                override fun save(order: Order) = order
                override fun findById(orderId: UUID) = null
                override fun findByPurchaseCode(clientId: UUID, purchaseCode: String) = null
                override fun findByClient(clientId: UUID) = emptyList<Order>()
                override fun findByClient(
                    clientId: UUID,
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
                    }
                }

                override fun findByBooking(bookingId: UUID) = emptyList<Order>()
                override fun findByContainer(containerId: UUID) = emptyList<Order>()
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
                    }
                }
            }
        }

        @Bean
        @Primary
        fun invoicePort(): co.com.nauta.usecase.port.InvoicePort {
            return object : co.com.nauta.usecase.port.InvoicePort {
                override fun save(invoice: Invoice) = invoice
                override fun findById(invoiceId: UUID) = null
                override fun findByCode(clientId: UUID, invoiceCode: String) = null
                override fun findByClient(clientId: UUID) = emptyList<Invoice>()
                override fun findByOrder(orderId: UUID) = emptyList<Invoice>()
            }
        }

        @Bean
        @Primary
        fun orderContainerPort(): co.com.nauta.usecase.port.OrderContainerPort {
            return object : co.com.nauta.usecase.port.OrderContainerPort {
                override fun save(orderContainer: OrderContainer) = orderContainer
                override fun findByOrderAndContainer(orderId: UUID, containerId: UUID) = null
                override fun findByOrder(orderId: UUID) = emptyList<OrderContainer>()
                override fun findByContainer(containerId: UUID) = emptyList<OrderContainer>()
                override fun deleteOrderContainer(orderId: UUID, containerId: UUID) {}
            }
        }
    }
}