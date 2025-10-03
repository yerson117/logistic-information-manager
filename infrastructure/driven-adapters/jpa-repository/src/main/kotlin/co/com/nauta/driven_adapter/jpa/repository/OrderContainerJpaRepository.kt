package co.com.nauta.driven_adapter.jpa.repository

import co.com.nauta.driven_adapter.jpa.entity.OrderContainerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * JPA Repository for OrderContainerEntity (junction table)
 * Following hexagonal architecture principles - one repository per entity
 */
@Repository
interface OrderContainerJpaRepository : JpaRepository<OrderContainerEntity, UUID> {
    fun findByOrderIdAndContainerId(orderId: UUID, containerId: UUID): OrderContainerEntity?
    fun findByOrderId(orderId: UUID): List<OrderContainerEntity>
    fun findByContainerId(containerId: UUID): List<OrderContainerEntity>
}
