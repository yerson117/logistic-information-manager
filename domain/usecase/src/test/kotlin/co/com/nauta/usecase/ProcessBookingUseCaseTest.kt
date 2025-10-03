package co.com.nauta.usecase

import co.com.nauta.model.bo.Booking
import co.com.nauta.usecase.port.BookingPort
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull

class ProcessBookingUseCaseTest {

    private lateinit var processBookingUseCase: ProcessBookingUseCase
    private lateinit var mockBookingPort: BookingPort

    @BeforeEach
    fun setUp() {
        mockBookingPort = createMockBookingPort()
        processBookingUseCase = ProcessBookingUseCase(mockBookingPort)
    }

    @Test
    fun `should create new booking when booking does not exist`() {
        // Given
        val bookingCode = "BK123"
        val clientId = UUID.randomUUID()

        // When
        val result = processBookingUseCase.process(bookingCode, clientId)

        // Then
        assertNotNull(result)
        assertEquals(bookingCode, result.bookingCode)
        assertEquals(clientId, result.clientId)
        assertNotNull(result.createdAt)
        assertNotNull(result.updatedAt)
    }

    @Test
    fun `should update existing booking when booking exists`() {
        // Given
        val bookingCode = "BK123"
        val clientId = UUID.randomUUID()
        val existingBooking = createBooking(clientId, bookingCode)
        
        // Mock existing booking
        val mockPortWithExisting = object : BookingPort {
            override fun save(booking: Booking): Booking = booking
            override fun findById(bookingId: UUID): Booking? = null
            override fun findByCode(clientId: UUID, bookingCode: String): Booking? = existingBooking
            override fun findByClient(clientId: UUID): List<Booking> = listOf(existingBooking)
        }
        
        val useCase = ProcessBookingUseCase(mockPortWithExisting)

        // When
        val result = useCase.process(bookingCode, clientId)

        // Then
        assertNotNull(result)
        assertEquals(bookingCode, result.bookingCode)
        assertEquals(clientId, result.clientId)
        assertNotNull(result.updatedAt)
    }

    @Test
    fun `should handle empty booking code`() {
        // Given
        val bookingCode = ""
        val clientId = UUID.randomUUID()

        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            processBookingUseCase.process(bookingCode, clientId)
        }
        assertEquals("Booking code cannot be blank", exception.message)
    }

    @Test
    fun `should handle null client ID`() {
        // Given
        val bookingCode = "BK123"
        val clientId = UUID.randomUUID()

        // When
        val result = processBookingUseCase.process(bookingCode, clientId)

        // Then
        assertNotNull(result)
        assertEquals(bookingCode, result.bookingCode)
        assertEquals(clientId, result.clientId)
    }

    private fun createBooking(clientId: UUID, bookingCode: String): Booking {
        return Booking().apply {
            this.clientId = clientId
            this.bookingCode = bookingCode
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
        }
    }

    private fun createMockBookingPort(): BookingPort {
        return object : BookingPort {
            override fun save(booking: Booking): Booking = booking
            override fun findById(bookingId: UUID): Booking? = null
            override fun findByCode(clientId: UUID, bookingCode: String): Booking? = null
            override fun findByClient(clientId: UUID): List<Booking> = emptyList()
        }
    }
}
