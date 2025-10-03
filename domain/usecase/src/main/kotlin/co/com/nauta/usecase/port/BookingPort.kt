package co.com.nauta.usecase.port

import co.com.nauta.model.bo.Booking
import java.util.UUID

/**
 * Port for Booking operations
 * Following hexagonal architecture principles - one port per entity
 */
interface BookingPort {
    fun save(booking: Booking): Booking
    fun findById(bookingId: UUID): Booking?
    fun findByCode(clientId: UUID, bookingCode: String): Booking?
    fun findByClient(clientId: UUID): List<Booking>
}
