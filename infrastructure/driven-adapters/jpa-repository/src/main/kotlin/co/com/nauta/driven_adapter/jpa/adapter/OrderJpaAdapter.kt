package co.com.nauta.driven_adapter.jpa.adapter

import co.com.nauta.driven_adapter.jpa.mapper.OrderMapper
import co.com.nauta.driven_adapter.jpa.repository.OrderJpaRepository
import co.com.nauta.model.bo.Order
import co.com.nauta.usecase.port.OrderPort
import co.com.nauta.usecase.port.Page
import co.com.nauta.usecase.port.Pageable
import org.springframework.data.domain.Page as SpringPage
import org.springframework.data.domain.Pageable as SpringPageable
import org.springframework.stereotype.Component
import java.util.UUID

/**
 * JPA Adapter implementation for OrderPort
 * Following hexagonal architecture principles - one adapter per port
 */
@Component
class OrderJpaAdapter(
    private val orderJpaRepository: OrderJpaRepository,
    private val orderMapper: OrderMapper
) : OrderPort {
    
    override fun save(order: Order): Order {
        val orderEntity = orderMapper.toEntity(order)
        val savedEntity = orderJpaRepository.save(orderEntity)
        return orderMapper.toDomain(savedEntity)
    }
    
    override fun findById(orderId: UUID): Order? {
        return orderJpaRepository.findById(orderId)
            .map { orderMapper.toDomain(it) }
            .orElse(null)
    }
    
    override fun findByPurchaseCode(clientId: UUID, purchaseCode: String): Order? {
        return orderJpaRepository.findByClientIdAndPurchaseCode(clientId, purchaseCode)
            ?.let { orderMapper.toDomain(it) }
    }
    
    override fun findByClient(clientId: UUID): List<Order> {
        return orderJpaRepository.findByClientId(clientId)
            .map { orderMapper.toDomain(it) }
    }
    
    override fun findByClient(clientId: UUID, pageable: Pageable): Page<Order> {
        // Convertir domain Pageable a Spring Pageable
        val springPageable = convertToSpringPageable(pageable)
        
        // Obtener página de Spring
        val springPage = orderJpaRepository.findByClientId(clientId, springPageable)
        
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
    
    private fun convertToDomainPage(springPage: SpringPage<co.com.nauta.driven_adapter.jpa.entity.OrderEntity>): Page<Order> {
        return object : Page<Order> {
            override val content: List<Order> = springPage.content.map { orderMapper.toDomain(it) }
            override val page: Int = springPage.number
            override val size: Int = springPage.size
            override val totalElements: Long = springPage.totalElements
            override val totalPages: Int = springPage.totalPages
            override val isFirst: Boolean = springPage.isFirst
            override val isLast: Boolean = springPage.isLast
            override val numberOfElements: Int = springPage.numberOfElements
        }
    }
    
    override fun findByBooking(bookingId: UUID): List<Order> {
        return orderJpaRepository.findByBookingId(bookingId)
            .map { orderMapper.toDomain(it) }
    }
    
    override fun findByContainer(containerId: UUID): List<Order> {
        return orderJpaRepository.findByContainerId(containerId)
            .map { orderMapper.toDomain(it) }
    }
    
    override fun findByContainer(containerId: UUID, pageable: Pageable): Page<Order> {
        // Convertir domain Pageable a Spring Pageable
        val springPageable = convertToSpringPageable(pageable)
        
        // Obtener página de Spring
        val springPage = orderJpaRepository.findByContainerId(containerId, springPageable)
        
        // Convertir a domain Page
        return convertToDomainPage(springPage)
    }
}
