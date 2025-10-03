package co.com.nauta.rest_api.mapper

import co.com.nauta.usecase.port.Page
import co.com.nauta.usecase.port.Pageable
import co.com.nauta.usecase.port.Sort
import co.com.nauta.usecase.port.SortDirection
import org.springframework.data.domain.Page as SpringPage
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable as SpringPageable
import org.springframework.data.domain.Sort as SpringSort
import org.springframework.stereotype.Component

/**
 * Mapper for converting between domain Pageable/Page and Spring Pageable/Page
 */
@Component
class PageableMapper {

    /**
     * Convert Spring Pageable to domain Pageable
     */
    fun toDomainPageable(springPageable: SpringPageable): Pageable {
        return object : Pageable {
            override val page: Int = springPageable.pageNumber
            override val size: Int = springPageable.pageSize
            override val sort: Sort? = if (springPageable.sort.isSorted) {
                val firstOrder = springPageable.sort.first()
                object : Sort {
                    override val property: String = firstOrder.property
                    override val direction: SortDirection = when (firstOrder.direction) {
                        SpringSort.Direction.ASC -> SortDirection.ASC
                        SpringSort.Direction.DESC -> SortDirection.DESC
                        else -> SortDirection.ASC
                    }
                }
            } else null
        }
    }

    /**
     * Convert domain Page to Spring Page
     */
    fun <T> toSpringPage(domainPage: Page<T>): SpringPage<T> {
        val pageable = PageRequest.of(domainPage.page, domainPage.size)
        return PageImpl(domainPage.content, pageable, domainPage.totalElements)
    }

    /**
     * Create Spring Pageable from parameters
     */
    fun createSpringPageable(page: Int, size: Int, sort: String?): SpringPageable {
        return if (sort != null) {
            val sortDirection = if (sort.startsWith("-")) SpringSort.Direction.DESC else SpringSort.Direction.ASC
            val sortField = if (sort.startsWith("-")) sort.substring(1) else sort
            PageRequest.of(page, size, SpringSort.by(sortDirection, sortField))
        } else {
            PageRequest.of(page, size)
        }
    }
}
