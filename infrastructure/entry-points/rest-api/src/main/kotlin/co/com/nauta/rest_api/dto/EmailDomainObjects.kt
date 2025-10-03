package co.com.nauta.rest_api.dto

import co.com.nauta.model.bo.Booking
import co.com.nauta.model.bo.Container
import co.com.nauta.model.bo.Invoice
import co.com.nauta.model.bo.Order
import co.com.nauta.usecase.ContainerRelation
import co.com.nauta.usecase.OrderRelation

data class EmailDomainObjects(
    val booking: Booking?,
    val containers: List<Container>,
    val orders: List<Pair<Order, List<Invoice>>>,
    val containerRelations: List<ContainerRelation>,
    val orderRelations: List<OrderRelation>
)