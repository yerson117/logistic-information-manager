package co.com.nauta.driven_adapter.jpa.repository

import co.com.nauta.driven_adapter.jpa.entity.OrderEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * JPA Repository for OrderEntity
 * Following hexagonal architecture principles - one repository per entity
 */
@Repository
interface OrderJpaRepository : JpaRepository<OrderEntity, UUID> {
    fun findByClientIdAndPurchaseCode(clientId: UUID, purchaseCode: String): OrderEntity?
    fun findByClientId(clientId: UUID): List<OrderEntity>
    fun findByClientId(clientId: UUID, pageable: Pageable): Page<OrderEntity>
    fun findByBookingId(bookingId: UUID): List<OrderEntity>
    
    @Query("""
        SELECT o FROM OrderEntity o 
        JOIN OrderContainerEntity oc ON o.orderId = oc.orderId 
        WHERE oc.containerId = :containerId
    """)
    fun findByContainerId(@Param("containerId") containerId: UUID): List<OrderEntity>
    
    @Query("""
        SELECT o FROM OrderEntity o 
        JOIN OrderContainerEntity oc ON o.orderId = oc.orderId 
        WHERE oc.containerId = :containerId
    """)
    fun findByContainerId(@Param("containerId") containerId: UUID, pageable: Pageable): Page<OrderEntity>
}
