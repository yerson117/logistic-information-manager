package co.com.nauta.driven_adapter.jpa.repository

import co.com.nauta.driven_adapter.jpa.entity.BookingEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * JPA Repository for BookingEntity
 * Following hexagonal architecture principles - one repository per entity
 */
@Repository
interface BookingJpaRepository : JpaRepository<BookingEntity, UUID> {
    fun findByClientIdAndBookingCode(clientId: UUID, bookingCode: String): BookingEntity?
    fun findByClientId(clientId: UUID): List<BookingEntity>
}
