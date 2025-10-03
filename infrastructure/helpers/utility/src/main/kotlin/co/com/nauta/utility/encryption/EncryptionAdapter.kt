package co.com.nauta.utility.encryption

import co.com.nauta.usecase.port.EncryptionPort
import org.springframework.stereotype.Component

@Component
class EncryptionAdapter(
    private val encryptionService: EncryptionService
) : EncryptionPort {
    
    override fun encryptPassword(password: String): String {
        return encryptionService.encryptPassword(password)
    }
    
    override fun matchesPassword(rawPassword: String, encodedPassword: String): Boolean {
        return encryptionService.matchesPassword(rawPassword, encodedPassword)
    }
}
