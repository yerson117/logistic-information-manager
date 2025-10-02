package co.com.nauta.driven_adapter.jpa.repository

import co.com.nauta.driven_adapter.jpa.entity.ClientEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * JPA Repository for ClientEntity
 * Following hexagonal architecture principles - one repository per entity
 */
@Repository
interface ClientJpaRepository : JpaRepository<ClientEntity, UUID> {
    fun findByEmail(email: String): ClientEntity?
}
