package co.com.nauta.usecase

import co.com.nauta.model.bo.Booking
import co.com.nauta.usecase.port.BookingPort
import java.time.LocalDateTime
import java.util.UUID

class ProcessBookingUseCase(
    private val bookingPort: BookingPort
) {
    
    fun process(bookingCode: String, clientId: UUID): Booking {
        if (bookingCode.isBlank()) {
            throw IllegalArgumentException("Booking code cannot be blank")
        }
        
        val existingBooking = bookingPort.findByCode(clientId, bookingCode)

        return if (existingBooking != null) {
            existingBooking.updatedAt = LocalDateTime.now()
            bookingPort.save(existingBooking)
        } else {
            val newBooking = Booking()
            newBooking.bookingCode = bookingCode
            newBooking.clientId = clientId
            newBooking.createdAt = LocalDateTime.now()
            newBooking.updatedAt = LocalDateTime.now()
            bookingPort.save(newBooking)
        }
    }
    
    fun process(bookingCode: String, clientId: UUID, bookingId: UUID?): Booking {
        return if (bookingId != null) {
            val existingBooking = bookingPort.findById(bookingId)
                ?: throw IllegalArgumentException("Booking with ID $bookingId not found")
            
            existingBooking.bookingCode = bookingCode
            existingBooking.updatedAt = LocalDateTime.now()
            bookingPort.save(existingBooking)
        } else {
            process(bookingCode, clientId)
        }
    }
}
