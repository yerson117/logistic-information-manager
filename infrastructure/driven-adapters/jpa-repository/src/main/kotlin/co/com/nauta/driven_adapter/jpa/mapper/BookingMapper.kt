package co.com.nauta.driven_adapter.jpa.mapper

import co.com.nauta.driven_adapter.jpa.entity.BookingEntity
import co.com.nauta.model.bo.Booking
import org.springframework.stereotype.Component

/**
 * Mapper between Booking domain entity (BO) and BookingEntity (JPA Entity)
 * Following hexagonal architecture principles
 */
@Component
class BookingMapper {
    
    fun toDomain(bookingEntity: BookingEntity): Booking {
        return Booking().apply {
            uuid = bookingEntity.bookingId
            clientId = bookingEntity.clientId
            bookingCode = bookingEntity.bookingCode
            createdAt = bookingEntity.createdAt
            updatedAt = bookingEntity.updatedAt
        }
    }
    
    fun toEntity(booking: Booking): BookingEntity {
        return BookingEntity(
            bookingId = booking.uuid,
            clientId = booking.clientId ?: throw IllegalArgumentException("Booking clientId cannot be null"),
            bookingCode = booking.bookingCode ?: throw IllegalArgumentException("Booking code cannot be null"),
            createdAt = booking.createdAt ?: throw IllegalArgumentException("Booking createdAt cannot be null"),
            updatedAt = booking.updatedAt ?: throw IllegalArgumentException("Booking updatedAt cannot be null")
        )
    }
}
