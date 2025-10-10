package co.com.nauta.rest_api.controller

import co.com.nauta.model.bo.Booking
import co.com.nauta.model.bo.Container
import co.com.nauta.model.bo.Order
import co.com.nauta.rest_api.dto.BookingResponseDto
import co.com.nauta.rest_api.dto.EmailDomainObjects
import co.com.nauta.rest_api.dto.EmailRequestDto
import co.com.nauta.rest_api.mapper.EmailRequestMapper
import co.com.nauta.rest_api.mapper.EmailResponseMapper
import co.com.nauta.usecase.ProcessEmailUseCase
import co.com.nauta.usecase.ProcessEmailResult
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime
import java.util.UUID
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EmailControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var processEmailUseCase: ProcessEmailUseCase

    @MockitoBean
    private lateinit var emailRequestMapper: EmailRequestMapper

    @MockitoBean
    private lateinit var emailResponseMapper: EmailResponseMapper

    @Test
    @WithMockUser(username = "550e8400-e29b-41d4-a716-446655440000", roles = ["CLIENT"])
    fun `should process email with booking only`() {
        // Given
        val clientId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
        val request = EmailRequestDto(
            booking = "BK123",
            containers = emptyList(),
            orders = emptyList()
        )
        
        val mockBooking = Booking().apply {
            this.uuid = UUID.randomUUID()
            this.bookingCode = "BK123"
            this.clientId = clientId
            this.createdAt = LocalDateTime.now()
            this.updatedAt = LocalDateTime.now()
        }
        
        // Mock the mappers and use case
        whenever(emailRequestMapper.toDomainObjects(any(), any())).thenReturn(
            EmailDomainObjects(
                booking = mockBooking,
                containers = emptyList(),
                orders = emptyList(),
                containerRelations = emptyList(),
                orderRelations = emptyList()
            )
        )
        
        whenever(processEmailUseCase.processEmail(any(), any(), any(), any(), any())).thenReturn(
            ProcessEmailResult(
                booking = mockBooking,
                containers = emptyList(),
                orders = emptyList()
            )
        )
        
        whenever(emailResponseMapper.toResponseDto(any(), any(), any(), any())).thenReturn(
            co.com.nauta.rest_api.dto.ProcessEmailResponseDto(
                booking = BookingResponseDto(
                    bookingId = mockBooking.uuid,
                    bookingCode = mockBooking.bookingCode!!,
                    clientId = mockBooking.clientId!!,
                    createdAt = mockBooking.createdAt,
                    updatedAt = mockBooking.updatedAt
                ),
                containers = emptyList(),
                orders = emptyList(),
                invoices = emptyList(),
                message = "Email processed successfully"
            )
        )

        // When & Then
        mockMvc.perform(
            post("/api/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.booking.bookingCode").value("BK123"))
            .andExpect(jsonPath("$.message").value("Email processed successfully"))
    }

    @Test
    fun `should return forbidden when no authentication`() {
        val request = EmailRequestDto(
            booking = "BK123",
            containers = emptyList(),
            orders = emptyList()
        )

        mockMvc.perform(
            post("/api/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer invalidtoken") // Invalid token
        )
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser
    fun `should return bad request for invalid JSON`() {
        mockMvc.perform(
            post("/api/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json")
        )
            .andExpect(status().isBadRequest)
    }
}
