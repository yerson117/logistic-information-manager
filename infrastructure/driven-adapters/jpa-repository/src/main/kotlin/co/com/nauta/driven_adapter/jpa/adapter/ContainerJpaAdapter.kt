package co.com.nauta.driven_adapter.jpa.adapter

import co.com.nauta.driven_adapter.jpa.mapper.ContainerMapper
import co.com.nauta.driven_adapter.jpa.repository.ContainerJpaRepository
import co.com.nauta.model.bo.Container
import co.com.nauta.usecase.port.ContainerPort
import co.com.nauta.usecase.port.Page
import co.com.nauta.usecase.port.Pageable
import org.springframework.data.domain.Page as SpringPage
import org.springframework.data.domain.Pageable as SpringPageable
import org.springframework.stereotype.Component
import java.util.UUID

/**
 * JPA Adapter implementation for ContainerPort
 * Following hexagonal architecture principles - one adapter per port
 */
@Component
class ContainerJpaAdapter(
    private val containerJpaRepository: ContainerJpaRepository,
    private val containerMapper: ContainerMapper
) : ContainerPort {
    
    override fun save(container: Container): Container {
        val containerEntity = containerMapper.toEntity(container)
        val savedEntity = containerJpaRepository.save(containerEntity)
        return containerMapper.toDomain(savedEntity)
    }
    
    override fun findById(containerId: UUID): Container? {
        return containerJpaRepository.findById(containerId)
            .map { containerMapper.toDomain(it) }
            .orElse(null)
    }
    
    override fun findByCode(clientId: UUID, containerCode: String): Container? {
        return containerJpaRepository.findByClientIdAndContainerCode(clientId, containerCode)
            ?.let { containerMapper.toDomain(it) }
    }
    
    override fun findByClient(clientId: UUID): List<Container> {
        return containerJpaRepository.findByClientId(clientId)
            .map { containerMapper.toDomain(it) }
    }
    
    override fun findByClient(clientId: UUID, pageable: Pageable): Page<Container> {
        // Convertir domain Pageable a Spring Pageable
        val springPageable = convertToSpringPageable(pageable)
        
        // Obtener página de Spring
        val springPage = containerJpaRepository.findByClientId(clientId, springPageable)
        
        // Convertir a domain Page
        return convertToDomainPage(springPage)
    }
    
    override fun findByBooking(bookingId: UUID): List<Container> {
        return containerJpaRepository.findByBookingId(bookingId)
            .map { containerMapper.toDomain(it) }
    }
    
    override fun findByOrder(orderId: UUID): List<Container> {
        return containerJpaRepository.findByOrderId(orderId)
            .map { containerMapper.toDomain(it) }
    }
    
    override fun findByOrder(orderId: UUID, pageable: Pageable): Page<Container> {
        // Convertir domain Pageable a Spring Pageable
        val springPageable = convertToSpringPageable(pageable)
        
        // Obtener página de Spring
        val springPage = containerJpaRepository.findByOrderId(orderId, springPageable)
        
        // Convertir a domain Page
        return convertToDomainPage(springPage)
    }
    
    private fun convertToSpringPageable(domainPageable: Pageable): SpringPageable {
        return org.springframework.data.domain.PageRequest.of(
            domainPageable.page,
            domainPageable.size,
            domainPageable.sort?.let { sort ->
                val direction = when (sort.direction) {
                    co.com.nauta.usecase.port.SortDirection.ASC -> org.springframework.data.domain.Sort.Direction.ASC
                    co.com.nauta.usecase.port.SortDirection.DESC -> org.springframework.data.domain.Sort.Direction.DESC
                }
                org.springframework.data.domain.Sort.by(direction, sort.property)
            } ?: org.springframework.data.domain.Sort.unsorted()
        )
    }
    
    private fun convertToDomainPage(springPage: SpringPage<co.com.nauta.driven_adapter.jpa.entity.ContainerEntity>): Page<Container> {
        return object : Page<Container> {
            override val content: List<Container> = springPage.content.map { containerMapper.toDomain(it) }
            override val page: Int = springPage.number
            override val size: Int = springPage.size
            override val totalElements: Long = springPage.totalElements
            override val totalPages: Int = springPage.totalPages
            override val isFirst: Boolean = springPage.isFirst
            override val isLast: Boolean = springPage.isLast
            override val numberOfElements: Int = springPage.numberOfElements
        }
    }
}
