package co.com.nauta.driven_adapter.jpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

/**
 * JPA Entity for Client
 * This represents the database table structure for Client domain entity
 */
@Entity
@Table(name = "clients")
data class ClientEntity(
    @Id
    @Column(name = "client_id")
    val clientId: UUID,
    
    @Column(name = "name", nullable = false)
    val name: String,
    
    @Column(name = "email", nullable = false, unique = true)
    val email: String,
    
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,
    
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime
) {
    constructor() : this(
        clientId = UUID.randomUUID(),
        name = "",
        email = "",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
}
