package co.com.nauta.usecase.port

import co.com.nauta.model.bo.User
import java.util.UUID

/**
 * Port for User operations (authentication)
 */
interface UserPort {
    fun save(user: User): User
    fun findById(userId: UUID): User?
    fun findByEmail(email: String): User?
    fun findByClientId(clientId: UUID): User?
}
