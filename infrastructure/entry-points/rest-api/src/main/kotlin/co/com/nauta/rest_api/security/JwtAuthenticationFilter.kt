package co.com.nauta.rest_api.security

import co.com.nauta.rest_api.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * JWT Authentication Filter
 * Validates JWT tokens and sets up Spring Security context
 */
@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            val token = authHeader.substring(7) // Remove "Bearer " prefix
            val userId = jwtService.extractUserId(token)
            
            if (userId.isNotEmpty() && SecurityContextHolder.getContext().authentication == null) {
                // Validate token and checksum
                if (jwtService.validateToken(token, userId)) {
                    // Create authentication object
                    val authToken = UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        emptyList()
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    
                    // Set authentication in security context
                    SecurityContextHolder.getContext().authentication = authToken
                    
                    // Add userId to request attributes for easy access in controllers
                    request.setAttribute("userId", userId)
                }
            }
        } catch (e: Exception) {
            // Log the error but don't fail the request
            logger.error("JWT validation failed", e)
        }

        filterChain.doFilter(request, response)
    }
}
