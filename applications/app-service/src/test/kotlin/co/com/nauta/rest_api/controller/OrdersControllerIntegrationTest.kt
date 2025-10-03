package co.com.nauta.rest_api.controller

import co.com.nauta.model.bo.Order
import co.com.nauta.usecase.GetOrdersUseCase
import co.com.nauta.usecase.port.Page
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime
import java.util.UUID
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrdersControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getOrdersUseCase: GetOrdersUseCase

    @Test
    fun `should return unauthorized when no authentication for orders`() {
        // When & Then
        mockMvc.perform(
            get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isForbidden)
    }

    @Test
    fun `should return unauthorized when no authentication for order containers`() {
        // When & Then
        mockMvc.perform(
            get("/api/orders/123e4567-e89b-12d3-a456-426614174000/containers")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser
    fun `should return bad request for invalid UUID in order containers`() {
        // When & Then
        mockMvc.perform(
            get("/api/orders/invalid-uuid/containers")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @WithMockUser(username = "550e8400-e29b-41d4-a716-446655440000", roles = ["CLIENT"])
    fun `should accept valid pagination parameters for orders`() {
        // Given
        val clientId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
        val mockOrder = Order().apply {
                    this.uuid = UUID.randomUUID()
            this.purchaseCode = "PO123"
            this.clientId = clientId
            this.createdAt = LocalDateTime.now()
            this.updatedAt = LocalDateTime.now()
        }
        val mockPage = object : Page<Order> {
            override val content: List<Order> = listOf(mockOrder)
            override val page: Int = 0
            override val size: Int = 10
            override val totalElements: Long = 1
            override val totalPages: Int = 1
            override val isFirst: Boolean = true
            override val isLast: Boolean = true
            override val numberOfElements: Int = 1
        }
        
        whenever(getOrdersUseCase.getOrdersByClient(any(), any())).thenReturn(mockPage)
        whenever(getOrdersUseCase.getInvoicesByOrder(any())).thenReturn(emptyList())
        
        // When & Then
        mockMvc.perform(
            get("/api/orders")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "purchaseCode,asc")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content[0].purchaseCode").value("PO123"))
    }
}
