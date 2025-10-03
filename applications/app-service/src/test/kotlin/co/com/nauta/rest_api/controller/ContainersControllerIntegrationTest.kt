package co.com.nauta.rest_api.controller

import co.com.nauta.model.bo.Container
import co.com.nauta.model.bo.Order
import co.com.nauta.usecase.GetContainersUseCase
import co.com.nauta.usecase.GetOrdersUseCase
import co.com.nauta.usecase.port.Page
import co.com.nauta.usecase.port.Pageable
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
class ContainersControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getContainersUseCase: GetContainersUseCase

    @MockitoBean
    private lateinit var getOrdersUseCase: GetOrdersUseCase

    @Test
    fun `should return unauthorized when no authentication for containers`() {
        // When & Then
        mockMvc.perform(
            get("/api/containers")
                .contentType(MediaType.APPLICATION_JSON)

        )
            .andExpect(status().isForbidden)
    }

    @Test
    fun `should return unauthorized when no authentication for container orders`() {
        // When & Then
        mockMvc.perform(
            get("/api/containers/123e4567-e89b-12d3-a456-426614174000/orders")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser
    fun `should return bad request for invalid UUID in container orders`() {
        // When & Then
        mockMvc.perform(
            get("/api/containers/invalid-uuid/orders")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @WithMockUser(username = "550e8400-e29b-41d4-a716-446655440000", roles = ["CLIENT"])
    fun `should accept valid pagination parameters for containers`() {
        // Given
        val clientId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
        val mockContainer = Container().apply{
            this.uuid = UUID.randomUUID()
            this.containerCode = "MEDU1234567"
            this.clientId = clientId
            this.createdAt = LocalDateTime.now()
            this.updatedAt = LocalDateTime.now()
        }
        
        val mockPage = object : Page<Container> {
            override val content: List<Container> = listOf(mockContainer)
            override val page: Int = 0
            override val size: Int = 10
            override val totalElements: Long = 1
            override val totalPages: Int = 1
            override val isFirst: Boolean = true
            override val isLast: Boolean = true
            override val numberOfElements: Int = 1
        }
        
        whenever(getContainersUseCase.getContainersByClient(any(), any())).thenReturn(mockPage)
        
        // When & Then
        mockMvc.perform(
            get("/api/containers")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "containerCode,asc")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content[0].containerCode").value("MEDU1234567"))
    }
}
