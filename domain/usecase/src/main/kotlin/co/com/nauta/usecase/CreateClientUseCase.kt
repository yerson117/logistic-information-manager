package co.com.nauta.usecase

import co.com.nauta.model.bo.Client
import co.com.nauta.usecase.exception.BusinessRuleViolationException
import co.com.nauta.usecase.port.ClientPort
import java.time.LocalDateTime
import java.util.UUID

class CreateClientUseCase(
    private val clientPort: ClientPort
) {
    
    fun execute(name: String, email: String): Client {
        validateClientData(name, email)

        if (clientPort.findByEmail(email) != null) {
            throw BusinessRuleViolationException("Client with email $email already exists")
        }
        
        val client = Client().apply {
            this.name = name
            this.email = email
            this.createdAt = LocalDateTime.now()
            this.updatedAt = LocalDateTime.now()
        }
        
        return clientPort.save(client)
    }
    
    private fun validateClientData(name: String, email: String) {
        if (name.isBlank()) {
            throw BusinessRuleViolationException("Client name cannot be blank")
        }
        if (email.isBlank()) {
            throw BusinessRuleViolationException("Client email cannot be blank")
        }
        if (!email.contains("@")) {
            throw BusinessRuleViolationException("Client email must be valid")
        }
    }
}
