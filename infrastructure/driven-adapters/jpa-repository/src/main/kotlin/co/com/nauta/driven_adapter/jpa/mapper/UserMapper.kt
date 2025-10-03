package co.com.nauta.driven_adapter.jpa.mapper

import co.com.nauta.driven_adapter.jpa.entity.UserEntity
import co.com.nauta.model.bo.User
import org.springframework.stereotype.Component

/**
 * Mapper between User domain entity (BO) and UserEntity (JPA Entity)
 * Following hexagonal architecture principles
 */
@Component
class UserMapper {
    
    fun toDomain(userEntity: UserEntity): User {
        return User().apply {
            uuid = userEntity.userId
            email = userEntity.email
            password = userEntity.password
            clientId = userEntity.clientId
            isActive = userEntity.isActive
        }
    }
    
    fun toEntity(user: User): UserEntity {
        return UserEntity(
            userId = user.uuid,
            email = user.email ?: throw IllegalArgumentException("User email cannot be null"),
            password = user.password ?: throw IllegalArgumentException("User password cannot be null"),
            clientId = user.clientId ?: throw IllegalArgumentException("User clientId cannot be null"),
            isActive = user.isActive
        )
    }
}
