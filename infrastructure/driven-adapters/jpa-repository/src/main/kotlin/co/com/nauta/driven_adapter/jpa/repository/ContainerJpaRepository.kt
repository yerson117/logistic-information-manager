package co.com.nauta.driven_adapter.jpa.repository

import co.com.nauta.driven_adapter.jpa.entity.ContainerEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * JPA Repository for ContainerEntity
 * Following hexagonal architecture principles - one repository per entity
 */
@Repository
interface ContainerJpaRepository : JpaRepository<ContainerEntity, UUID> {
    fun findByClientIdAndContainerCode(clientId: UUID, containerCode: String): ContainerEntity?
    fun findByClientId(clientId: UUID): List<ContainerEntity>
    fun findByClientId(clientId: UUID, pageable: Pageable): Page<ContainerEntity>
    fun findByBookingId(bookingId: UUID): List<ContainerEntity>
    
    @Query("""
        SELECT c FROM ContainerEntity c 
        JOIN OrderContainerEntity oc ON c.containerId = oc.containerId 
        WHERE oc.orderId = :orderId
    """)
    fun findByOrderId(@Param("orderId") orderId: UUID): List<ContainerEntity>
    
    @Query("""
        SELECT c FROM ContainerEntity c 
        JOIN OrderContainerEntity oc ON c.containerId = oc.containerId 
        WHERE oc.orderId = :orderId
    """)
    fun findByOrderId(@Param("orderId") orderId: UUID, pageable: Pageable): Page<ContainerEntity>
}
