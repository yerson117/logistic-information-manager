package co.com.nauta.model.bo

/**
 * CLIENT entity representing a client in the logistics system
 * 
 * Attributes:
 * - client_id: UUID, Primary Key
 * - name: String, client name
 * - email: String, client email
 * - created_at: LocalDateTime, creation timestamp
 * - updated_at: LocalDateTime, last update timestamp
 */
class Client : BaseModelBO() {
    var name: String? = null
    var email: String? = null
}
