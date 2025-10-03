package co.com.nauta.usecase.port

/**
 * Port for encryption operations
 */
interface EncryptionPort {
    fun encryptPassword(password: String): String
    fun matchesPassword(rawPassword: String, encodedPassword: String): Boolean
}
