package co.com.nauta.rest_api.controller

import co.com.nauta.rest_api.dto.AuthRequestDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should return bad request for invalid JSON in login`() {
        // When & Then
        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return bad request for invalid JSON in validate`() {
        // When & Then
        mockMvc.perform(
            post("/api/auth/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return bad request for empty password in login`() {
        // Given
        val request = AuthRequestDto(
            userId = UUID.randomUUID(),
            password = ""
        )

        // When & Then
        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return bad request for null password in login`() {
        // Given
        val request = mapOf(
            "userId" to UUID.randomUUID().toString(),
            "password" to null
        )

        // When & Then
        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }
}
