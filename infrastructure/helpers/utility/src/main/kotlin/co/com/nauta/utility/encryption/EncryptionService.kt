package co.com.nauta.utility.encryption

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class EncryptionService(
    private val passwordEncoder: PasswordEncoder
) {
    fun encryptPassword(password: String): String {
        return passwordEncoder.encode(password)
    }
    
    fun matchesPassword(rawPassword: String, encodedPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword)
    }
}
