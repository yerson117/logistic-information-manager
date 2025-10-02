package co.com.nauta.rest_api.dto

/**
 * DTO for pagination request parameters
 */
data class PageRequestDto(
    val page: Int = 0,
    val size: Int = 20,
    val sort: String? = null
) {
    init {
        require(page >= 0) { "Page number must be non-negative" }
        require(size > 0 && size <= 100) { "Size must be between 1 and 100" }
    }
}
