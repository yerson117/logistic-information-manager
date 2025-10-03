package co.com.nauta.driven_adapter.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

/**
 * JPA Entity for User
 * This represents the database table structure for User domain entity
 */
@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @Column(name = "user_id")
    val userId: UUID,
    
    @Column(name = "email", nullable = false, unique = true)
    val email: String,
    
    @Column(name = "password", nullable = false)
    val password: String,
    
    @Column(name = "client_id", nullable = false)
    val clientId: UUID,
    
    @Column(name = "is_active", nullable = false)
    val isActive: Boolean
) {
    constructor() : this(UUID.randomUUID(), "", "", UUID.randomUUID(), true)
}
