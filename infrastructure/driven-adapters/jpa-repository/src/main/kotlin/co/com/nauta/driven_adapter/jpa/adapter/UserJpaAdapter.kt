package co.com.nauta.driven_adapter.jpa.adapter

import co.com.nauta.driven_adapter.jpa.mapper.UserMapper
import co.com.nauta.driven_adapter.jpa.repository.UserJpaRepository
import co.com.nauta.model.bo.User
import co.com.nauta.usecase.port.UserPort
import org.springframework.stereotype.Component
import java.util.UUID

/**
 * JPA Adapter implementation for UserPort
 * Following hexagonal architecture principles - one adapter per port
 */
@Component
class UserJpaAdapter(
    private val userJpaRepository: UserJpaRepository,
    private val userMapper: UserMapper
) : UserPort {
    
    override fun save(user: User): User {
        val userEntity = userMapper.toEntity(user)
        val savedEntity = userJpaRepository.save(userEntity)
        return userMapper.toDomain(savedEntity)
    }
    
    override fun findById(userId: UUID): User? {
        return userJpaRepository.findById(userId)
            .map { userMapper.toDomain(it) }
            .orElse(null)
    }
    
    override fun findByEmail(email: String): User? {
        return userJpaRepository.findByEmail(email)
            ?.let { userMapper.toDomain(it) }
    }
    
    override fun findByClientId(clientId: UUID): User? {
        return userJpaRepository.findByClientId(clientId)
            ?.let { userMapper.toDomain(it) }
    }
}
