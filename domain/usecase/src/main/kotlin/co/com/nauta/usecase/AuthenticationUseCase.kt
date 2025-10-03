package co.com.nauta.usecase

import co.com.nauta.model.bo.User
import co.com.nauta.usecase.exception.BusinessRuleViolationException
import co.com.nauta.usecase.port.UserPort
import co.com.nauta.usecase.port.EncryptionPort
import co.com.nauta.usecase.port.ClientPort
import java.time.LocalDateTime
import java.util.UUID

class AuthenticationUseCase(
    private val userPort: UserPort,
    private val encryptionPort: EncryptionPort,
    private val clientPort: ClientPort
) {
    
    fun authenticate(email: String, password: String): User? {
        if (email.isBlank() || password.isBlank()) {
            return null
        }
        
        val user = userPort.findByEmail(email) ?: return null
        val userPassword = user.password
        
        return if (user.isActive && userPassword != null && encryptionPort.matchesPassword(password, userPassword)) {
            user
        } else {
            null
        }
    }
    
    fun register(email: String, password: String, clientId: UUID): User {
        validateRegistrationData(email, password)

        if (userPort.findByEmail(email) != null) {
            throw BusinessRuleViolationException("User with email $email already exists")
        }

        clientPort.findById(clientId) ?: throw BusinessRuleViolationException("Client with ID $clientId not found")

        val user = User().apply {
            this.email = email
            this.password = encryptionPort.encryptPassword(password)
            this.clientId = clientId
            this.isActive = true
            this.createdAt = LocalDateTime.now()
            this.updatedAt = LocalDateTime.now()
        }
        
        return userPort.save(user)
    }
    
    private fun validateRegistrationData(email: String, password: String) {
        if (email.isBlank()) {
            throw BusinessRuleViolationException("Email cannot be blank")
        }
        if (password.isBlank() || password.length < 6) {
            throw BusinessRuleViolationException("Password must be at least 6 characters")
        }
    }
}
