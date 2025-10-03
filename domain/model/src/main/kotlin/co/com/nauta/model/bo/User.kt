package co.com.nauta.model.bo

import java.util.UUID

/**
 * USER entity representing a user in the authentication system
 * 
 * Attributes:
 * - user_id: UUID, Primary Key
 * - email: String, unique user email
 * - password: String, encrypted password
 * - client_id: UUID, Foreign Key to CLIENT
 * - is_active: Boolean, user status
 * - created_at: LocalDateTime, creation timestamp
 * - updated_at: LocalDateTime, last update timestamp
 * 
 * Relationships:
 * - USER to CLIENT: "belongs to" (belongs to). A USER belongs to one CLIENT
 */
class User : BaseModelBO() {
    var email: String? = null
    var password: String? = null
    var clientId: UUID? = null
    var isActive: Boolean = true
}
