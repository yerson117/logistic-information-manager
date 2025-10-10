package co.com.nauta.usecase.port

/**
 * Domain interface for paginated results
 * This keeps the domain layer clean of Spring Framework dependencies
 */
interface Page<T> {
    val content: List<T>
    val page: Int
    val size: Int
    val totalElements: Long
    val totalPages: Int
    val isFirst: Boolean
    val isLast: Boolean
    val numberOfElements: Int
}
