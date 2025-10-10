package co.com.nauta.usecase

import co.com.nauta.model.bo.Client
import co.com.nauta.usecase.port.ClientPort
import java.util.UUID

class GetClientUseCase(
    private val clientPort: ClientPort
) {
    
    fun getById(clientId: UUID): Client? {
        return clientPort.findById(clientId)
    }
    
    fun getByEmail(email: String): Client? {
        return clientPort.findByEmail(email)
    }
    
    fun getAll(): List<Client> {
        return clientPort.findAll()
    }
}
