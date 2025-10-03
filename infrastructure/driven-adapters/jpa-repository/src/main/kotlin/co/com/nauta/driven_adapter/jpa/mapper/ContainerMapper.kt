package co.com.nauta.driven_adapter.jpa.mapper

import co.com.nauta.driven_adapter.jpa.entity.ContainerEntity
import co.com.nauta.model.bo.Container
import org.springframework.stereotype.Component

/**
 * Mapper between Container domain entity (BO) and ContainerEntity (JPA Entity)
 * Following hexagonal architecture principles
 */
@Component
class ContainerMapper {
    
    fun toDomain(containerEntity: ContainerEntity): Container {
        return Container().apply {
            uuid = containerEntity.containerId
            clientId = containerEntity.clientId
            bookingId = containerEntity.bookingId
            containerCode = containerEntity.containerCode
            createdAt = containerEntity.createdAt
            updatedAt = containerEntity.updatedAt
        }
    }
    
    fun toEntity(container: Container): ContainerEntity {
        return ContainerEntity(
            containerId = container.uuid,
            clientId = container.clientId ?: throw IllegalArgumentException("Container clientId cannot be null"),
            bookingId = container.bookingId,
            containerCode = container.containerCode ?: throw IllegalArgumentException("Container code cannot be null"),
            createdAt = container.createdAt ?: throw IllegalArgumentException("Container createdAt cannot be null"),
            updatedAt = container.updatedAt ?: throw IllegalArgumentException("Container updatedAt cannot be null")
        )
    }
}
