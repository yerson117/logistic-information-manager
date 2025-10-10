package co.com.nauta.rest_api.controller

import co.com.nauta.rest_api.dto.OrderResponseDto
import co.com.nauta.rest_api.dto.PageResponseDto
import co.com.nauta.rest_api.mapper.EmailResponseMapper
import co.com.nauta.rest_api.mapper.PageableMapper
import co.com.nauta.usecase.GetOrdersUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/orders", produces = [MediaType.APPLICATION_JSON_VALUE])
class OrdersController(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val emailResponseMapper: EmailResponseMapper,
    private val pageableMapper: PageableMapper
) {

    @GetMapping
    fun getOrdersByClient(
        authentication: Authentication?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) sort: String?
    ): ResponseEntity<PageResponseDto<OrderResponseDto>> {
        return try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }
            
            val userId = authentication.name
            val clientUuid = UUID.fromString(userId)
            
            val springPageable = pageableMapper.createSpringPageable(page, size, sort)
            val domainPageable = pageableMapper.toDomainPageable(springPageable)
            
            val ordersPage = getOrdersUseCase.getOrdersByClient(clientUuid, domainPageable)
            
            val orderDtos = ordersPage.content.map { order ->
                val invoices = getOrdersUseCase.getInvoicesByOrder(order.uuid)
                emailResponseMapper.toOrderResponseDto(order, invoices)
            }
            
            val pageResponse = PageResponseDto(
                content = orderDtos,
                page = ordersPage.page,
                size = ordersPage.size,
                totalElements = ordersPage.totalElements,
                totalPages = ordersPage.totalPages,
                first = ordersPage.isFirst,
                last = ordersPage.isLast,
                numberOfElements = ordersPage.numberOfElements
            )
            
            ResponseEntity.ok(pageResponse)
            
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/{purchaseId}/containers")
    fun getContainersByOrder(
        authentication: Authentication?,
        @PathVariable purchaseId: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) sort: String?
    ): ResponseEntity<PageResponseDto<co.com.nauta.rest_api.dto.ContainerResponseDto>> {
        return try {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }
            
            val userId = authentication.name
            val clientUuid = UUID.fromString(userId)
            
            val order = getOrdersUseCase.getOrderByPurchaseCodeOrThrow(clientUuid, purchaseId)
            
            val springPageable = pageableMapper.createSpringPageable(page, size, sort)
            val domainPageable = pageableMapper.toDomainPageable(springPageable)
            
            val containersPage = getOrdersUseCase.getContainersByOrder(order.uuid, domainPageable)
            
            val containerDtos = containersPage.content.map { container ->
                emailResponseMapper.toContainerResponseDto(container)
            }
            
            val pageResponse = PageResponseDto(
                content = containerDtos,
                page = containersPage.page,
                size = containersPage.size,
                totalElements = containersPage.totalElements,
                totalPages = containersPage.totalPages,
                first = containersPage.isFirst,
                last = containersPage.isLast,
                numberOfElements = containersPage.numberOfElements
            )
            
            ResponseEntity.ok(pageResponse)
            
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}
