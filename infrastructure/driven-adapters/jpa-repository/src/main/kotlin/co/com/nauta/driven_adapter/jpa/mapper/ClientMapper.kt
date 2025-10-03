package co.com.nauta.driven_adapter.jpa.mapper

import co.com.nauta.driven_adapter.jpa.entity.ClientEntity
import co.com.nauta.model.bo.Client
import org.springframework.stereotype.Component

/**
 * Mapper between Client domain entity (BO) and ClientEntity (JPA Entity)
 * Following hexagonal architecture principles
 */
@Component
class ClientMapper {
    
    fun toDomain(clientEntity: ClientEntity): Client {
        return Client().apply {
            uuid = clientEntity.clientId
            name = clientEntity.name
            email = clientEntity.email
            createdAt = clientEntity.createdAt
            updatedAt = clientEntity.updatedAt
        }
    }
    
    fun toEntity(client: Client): ClientEntity {
        return ClientEntity(
            clientId = client.uuid,
            name = client.name ?: throw IllegalArgumentException("Client name cannot be null"),
            email = client.email ?: throw IllegalArgumentException("Client email cannot be null"),
            createdAt = client.createdAt!!,
            updatedAt = client.updatedAt!!
        )
    }
}
