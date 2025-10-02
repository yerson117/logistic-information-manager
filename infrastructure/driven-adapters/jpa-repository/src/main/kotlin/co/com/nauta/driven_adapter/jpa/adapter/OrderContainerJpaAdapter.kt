package co.com.nauta.driven_adapter.jpa.adapter

import co.com.nauta.driven_adapter.jpa.mapper.OrderContainerMapper
import co.com.nauta.driven_adapter.jpa.repository.OrderContainerJpaRepository
import co.com.nauta.model.bo.OrderContainer
import co.com.nauta.usecase.port.OrderContainerPort
import org.springframework.stereotype.Component
import java.util.UUID

/**
 * JPA Adapter implementation for OrderContainerPort
 * Following hexagonal architecture principles - one adapter per port
 */
@Component
class OrderContainerJpaAdapter(
    private val orderContainerJpaRepository: OrderContainerJpaRepository,
    private val orderContainerMapper: OrderContainerMapper
) : OrderContainerPort {
    
    override fun save(orderContainer: OrderContainer): OrderContainer {
        val orderContainerEntity = orderContainerMapper.toEntity(orderContainer)
        val savedEntity = orderContainerJpaRepository.save(orderContainerEntity)
        return orderContainerMapper.toDomain(savedEntity)
    }
    
    override fun findByOrderAndContainer(orderId: UUID, containerId: UUID): OrderContainer? {
        return orderContainerJpaRepository.findByOrderIdAndContainerId(orderId, containerId)
            ?.let { orderContainerMapper.toDomain(it) }
    }
    
    override fun findByOrder(orderId: UUID): List<OrderContainer> {
        return orderContainerJpaRepository.findByOrderId(orderId)
            .map { orderContainerMapper.toDomain(it) }
    }
    
    override fun findByContainer(containerId: UUID): List<OrderContainer> {
        return orderContainerJpaRepository.findByContainerId(containerId)
            .map { orderContainerMapper.toDomain(it) }
    }
    
    override fun deleteOrderContainer(orderId: UUID, containerId: UUID) {
        val relation = orderContainerJpaRepository.findByOrderIdAndContainerId(orderId, containerId)
        relation?.let { orderContainerJpaRepository.delete(it) }
    }
}
