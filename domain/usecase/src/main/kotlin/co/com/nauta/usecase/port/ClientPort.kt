package co.com.nauta.usecase.port

import co.com.nauta.model.bo.Client
import java.util.UUID

/**
 * Port for Client operations
 * Following hexagonal architecture principles - one port per entity
 */
interface ClientPort {
    fun save(client: Client): Client
    fun findById(clientId: UUID): Client?
    fun findByEmail(email: String): Client?
}
