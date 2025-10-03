package co.com.nauta.rest_api.dto

/**
 * DTO for paginated response
 */
data class PageResponseDto<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val first: Boolean,
    val last: Boolean,
    val numberOfElements: Int
)
