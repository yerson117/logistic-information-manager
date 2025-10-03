package co.com.nauta.rest_api.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.crypto.SecretKey
import java.util.Date
import java.util.UUID

/**
 * Service for JWT token generation and validation
 */
@Service
class JwtService {

    @Value("\${jwt.secret:mySecretKey123456789012345678901234567890}")
    private lateinit var secretKey: String

    @Value("\${jwt.expiration:86400000}") // 24 hours in milliseconds
    private lateinit var expiration: String

    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(secretKey.toByteArray())
    }

    /**
     * Generate JWT token with userId and checksum
     */
    fun generateToken(userId: UUID): String {
        val now = Date()
        val expirationDate = Date(now.time + expiration.toLong())
        
        // Generate checksum for data integrity
        val checksum = generateChecksum(userId)
        
        return Jwts.builder()
            .subject(userId.toString())
            .claim("userId", userId)
            .claim("checksum", checksum)
            .issuedAt(now)
            .expiration(expirationDate)
            .signWith(key)
            .compact()
    }

    /**
     * Extract userId from JWT token
     */
    fun extractUserId(token: String): UUID {
        return  UUID.fromString(extractClaim(token) { it.subject })
    }

    /**
     * Extract checksum from JWT token
     */
    fun extractChecksum(token: String): String {
        return extractClaim(token) { it["checksum"] as String }
    }

    /**
     * Validate JWT token and checksum integrity
     */
    fun validateToken(token: String, userId: UUID): Boolean {
        return try {
            val extractedUserId = extractUserId(token)
            val extractedChecksum = extractChecksum(token)
            val expectedChecksum = generateChecksum(userId)
            
            // Validate user ID matches and checksum is valid
            extractedUserId == userId && extractedChecksum == expectedChecksum && !isTokenExpired(token)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Check if token is expired
     */
    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    /**
     * Extract expiration date from token
     */
    private fun extractExpiration(token: String): Date {
        return extractClaim(token) { it.expiration }
    }

    /**
     * Extract specific claim from token
     */
    private fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    /**
     * Extract all claims from token
     */
    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    /**
     * Generate checksum for data integrity validation
     * This creates a hash based on userId and a secret salt
     */
    private fun generateChecksum(userId: UUID): String {
        val salt = "nauta_logistics_2025"
        val dataToHash = "$userId:$salt:${System.currentTimeMillis() / (1000 * 60 * 60 * 24)}" // Daily rotation
        return dataToHash.hashCode().toString()
    }

    /**
     * Get token expiration time in seconds
     */
    fun getExpirationTime(): Long {
        return expiration.toLong() / 1000
    }
}
