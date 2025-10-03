package co.com.nauta.driven_adapter.jpa.adapter

import co.com.nauta.driven_adapter.jpa.mapper.ClientMapper
import co.com.nauta.driven_adapter.jpa.repository.ClientJpaRepository
import co.com.nauta.model.bo.Client
import co.com.nauta.usecase.port.ClientPort
import org.springframework.stereotype.Component
import java.util.UUID

/**
 * JPA Adapter implementation for ClientPort
 * Following hexagonal architecture principles - one adapter per port
 */
@Component
class ClientJpaAdapter(
    private val clientJpaRepository: ClientJpaRepository,
    private val clientMapper: ClientMapper
) : ClientPort {
    
    override fun save(client: Client): Client {
        val clientEntity = clientMapper.toEntity(client)
        val savedEntity = clientJpaRepository.save(clientEntity)
        return clientMapper.toDomain(savedEntity)
    }
    
    override fun findById(clientId: UUID): Client? {
        return clientJpaRepository.findById(clientId)
            .map { clientMapper.toDomain(it) }
            .orElse(null)
    }
    
    override fun findByEmail(email: String): Client? {
        return clientJpaRepository.findByEmail(email)
            ?.let { clientMapper.toDomain(it) }
    }
    
    override fun findAll(): List<Client> {
        return clientJpaRepository.findAll()
            .map { clientMapper.toDomain(it) }
    }
}
