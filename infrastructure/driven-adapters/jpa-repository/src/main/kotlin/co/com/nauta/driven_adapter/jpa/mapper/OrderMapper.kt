package co.com.nauta.driven_adapter.jpa.mapper

import co.com.nauta.driven_adapter.jpa.entity.OrderEntity
import co.com.nauta.model.bo.Order
import org.springframework.stereotype.Component

/**
 * Mapper between Order domain entity (BO) and OrderEntity (JPA Entity)
 * Following hexagonal architecture principles
 */
@Component
class OrderMapper {
    
    fun toDomain(orderEntity: OrderEntity): Order {
        return Order().apply {
            uuid = orderEntity.orderId
            clientId = orderEntity.clientId
            bookingId = orderEntity.bookingId
            purchaseCode = orderEntity.purchaseCode
            createdAt = orderEntity.createdAt
            updatedAt = orderEntity.updatedAt
        }
    }
    
    fun toEntity(order: Order): OrderEntity {
        return OrderEntity(
            orderId = order.uuid,
            clientId = order.clientId ?: throw IllegalArgumentException("Order clientId cannot be null"),
            bookingId = order.bookingId,
            purchaseCode = order.purchaseCode ?: throw IllegalArgumentException("Order purchase code cannot be null"),
            createdAt = order.createdAt ?: throw IllegalArgumentException("Order createdAt cannot be null"),
            updatedAt = order.updatedAt ?: throw IllegalArgumentException("Order updatedAt cannot be null")
        )
    }
}
