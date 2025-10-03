package co.com.nauta.driven_adapter.jpa.mapper

import co.com.nauta.driven_adapter.jpa.entity.OrderContainerEntity
import co.com.nauta.model.bo.OrderContainer
import org.springframework.stereotype.Component

/**
 * Mapper between OrderContainer domain entity (BO) and OrderContainerEntity (JPA Entity)
 * Following hexagonal architecture principles
 */
@Component
class OrderContainerMapper {
    
    fun toDomain(orderContainerEntity: OrderContainerEntity): OrderContainer {
        return OrderContainer().apply {
            orderId = orderContainerEntity.orderId
            containerId = orderContainerEntity.containerId
            createdAt = orderContainerEntity.createdAt
            updatedAt = orderContainerEntity.updatedAt
        }
    }
    
    fun toEntity(orderContainer: OrderContainer): OrderContainerEntity {
        return OrderContainerEntity(
            orderId = orderContainer.orderId ?: throw IllegalArgumentException("OrderContainer orderId cannot be null"),
            containerId = orderContainer.containerId ?: throw IllegalArgumentException("OrderContainer containerId cannot be null"),
            createdAt = orderContainer.createdAt ?: throw IllegalArgumentException("OrderContainer createdAt cannot be null"),
            updatedAt = orderContainer.updatedAt ?: throw IllegalArgumentException("OrderContainer updatedAt cannot be null")
        )
    }
}
