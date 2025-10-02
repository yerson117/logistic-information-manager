package co.com.nauta.usecase.port

/**
 * Domain interface for pagination
 * This keeps the domain layer clean of Spring Framework dependencies
 */
interface Pageable {
    val page: Int
    val size: Int
    val sort: Sort?
}

/**
 * Domain interface for sorting
 */
interface Sort {
    val property: String
    val direction: SortDirection
}

/**
 * Sort direction enum
 */
enum class SortDirection {
    ASC, DESC
}
