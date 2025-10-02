package co.com.nauta.driven_adapter.jpa.adapter

import co.com.nauta.driven_adapter.jpa.mapper.BookingMapper
import co.com.nauta.driven_adapter.jpa.repository.BookingJpaRepository
import co.com.nauta.model.bo.Booking
import co.com.nauta.usecase.port.BookingPort
import org.springframework.stereotype.Component
import java.util.UUID

/**
 * JPA Adapter implementation for BookingPort
 * Following hexagonal architecture principles - one adapter per port
 */
@Component
class BookingJpaAdapter(
    private val bookingJpaRepository: BookingJpaRepository,
    private val bookingMapper: BookingMapper
) : BookingPort {
    
    override fun save(booking: Booking): Booking {
        val bookingEntity = bookingMapper.toEntity(booking)
        val savedEntity = bookingJpaRepository.save(bookingEntity)
        return bookingMapper.toDomain(savedEntity)
    }
    
    override fun findById(bookingId: UUID): Booking? {
        return bookingJpaRepository.findById(bookingId)
            .map { bookingMapper.toDomain(it) }
            .orElse(null)
    }
    
    override fun findByCode(clientId: UUID, bookingCode: String): Booking? {
        return bookingJpaRepository.findByClientIdAndBookingCode(clientId, bookingCode)
            ?.let { bookingMapper.toDomain(it) }
    }
    
    override fun findByClient(clientId: UUID): List<Booking> {
        return bookingJpaRepository.findByClientId(clientId)
            .map { bookingMapper.toDomain(it) }
    }
}
