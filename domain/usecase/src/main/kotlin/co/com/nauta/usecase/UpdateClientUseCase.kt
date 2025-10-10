package co.com.nauta.usecase

import co.com.nauta.model.bo.Client
import co.com.nauta.usecase.exception.BusinessRuleViolationException
import co.com.nauta.usecase.port.ClientPort
import java.time.LocalDateTime
import java.util.UUID

class UpdateClientUseCase(
    private val clientPort: ClientPort
) {
    
    fun execute(clientId: UUID, name: String?, email: String?): Client {
        validateUpdateData(name, email)
        
        val existingClient = clientPort.findById(clientId)
            ?: throw BusinessRuleViolationException("Client with ID $clientId not found")

        if (email != null && email != existingClient.email) {
            val clientWithEmail = clientPort.findByEmail(email)
            if (clientWithEmail != null && clientWithEmail.uuid != clientId) {
                throw BusinessRuleViolationException("Client with email $email already exists")
            }
        }
        
        val updatedClient = existingClient.apply {
            if (name != null) this.name = name
            if (email != null) this.email = email
            this.updatedAt = LocalDateTime.now()
        }
        
        return clientPort.save(updatedClient)
    }
    
    private fun validateUpdateData(name: String?, email: String?) {
        if (name != null && name.isBlank()) {
            throw BusinessRuleViolationException("Client name cannot be blank")
        }
        if (email != null && email.isBlank()) {
            throw BusinessRuleViolationException("Client email cannot be blank")
        }
        if (email != null && !email.contains("@")) {
            throw BusinessRuleViolationException("Client email must be valid")
        }
    }
}
