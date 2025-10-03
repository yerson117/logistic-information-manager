package co.com.nauta.usecase.port

import co.com.nauta.model.bo.Container
import java.util.UUID

/**
 * Port for Container operations
 */
interface ContainerPort {
    fun save(container: Container): Container
    fun findById(containerId: UUID): Container?
    fun findByCode(clientId: UUID, containerCode: String): Container?
    fun findByClient(clientId: UUID): List<Container>
    fun findByClient(clientId: UUID, pageable: Pageable): Page<Container>
    fun findByBooking(bookingId: UUID): List<Container>
    fun findByOrder(orderId: UUID): List<Container>
    fun findByOrder(orderId: UUID, pageable: Pageable): Page<Container>
}
