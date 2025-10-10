package co.com.nauta.usecase.port

import co.com.nauta.model.bo.Booking
import java.util.UUID

/**
 * Port for Booking operations
 */
interface BookingPort {
    fun save(booking: Booking): Booking
    fun findById(bookingId: UUID): Booking?
    fun findByCode(clientId: UUID, bookingCode: String): Booking?
    fun findByClient(clientId: UUID): List<Booking>
}
