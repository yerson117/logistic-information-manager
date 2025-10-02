package co.com.nauta.config

import co.com.nauta.usecase.ProcessBookingUseCase
import co.com.nauta.usecase.ProcessContainerUseCase
import co.com.nauta.usecase.ProcessEmailUseCase
import co.com.nauta.usecase.ProcessInvoiceUseCase
import co.com.nauta.usecase.ProcessOrderUseCase
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@RequiredArgsConstructor
class UseCasesConfig {

    @Bean
    fun processBookingUseCase(bookingPort: co.com.nauta.usecase.port.BookingPort): ProcessBookingUseCase {
        return ProcessBookingUseCase(bookingPort)
    }

    @Bean
    fun processContainerUseCase(
        containerPort: co.com.nauta.usecase.port.ContainerPort,
        orderContainerPort: co.com.nauta.usecase.port.OrderContainerPort
    ): ProcessContainerUseCase {
        return ProcessContainerUseCase(containerPort, orderContainerPort)
    }

    @Bean
    fun processOrderUseCase(
        orderPort: co.com.nauta.usecase.port.OrderPort,
        orderContainerPort: co.com.nauta.usecase.port.OrderContainerPort
    ): ProcessOrderUseCase {
        return ProcessOrderUseCase(orderPort, orderContainerPort)
    }

    @Bean
    fun processInvoiceUseCase(invoicePort: co.com.nauta.usecase.port.InvoicePort): ProcessInvoiceUseCase {
        return ProcessInvoiceUseCase(invoicePort)
    }

    @Bean
    fun processEmailUseCase(
        processBookingUseCase: ProcessBookingUseCase,
        processContainerUseCase: ProcessContainerUseCase,
        processOrderUseCase: ProcessOrderUseCase,
        processInvoiceUseCase: ProcessInvoiceUseCase,
        containerPort: co.com.nauta.usecase.port.ContainerPort,
        orderPort: co.com.nauta.usecase.port.OrderPort
    ): ProcessEmailUseCase {
        return ProcessEmailUseCase(
            processBookingUseCase,
            processContainerUseCase,
            processOrderUseCase,
            processInvoiceUseCase,
            containerPort,
            orderPort
        )
    }

    @Bean
    fun getOrdersUseCase(
        orderPort: co.com.nauta.usecase.port.OrderPort,
        containerPort: co.com.nauta.usecase.port.ContainerPort,
        invoicePort: co.com.nauta.usecase.port.InvoicePort
    ): co.com.nauta.usecase.GetOrdersUseCase {
        return co.com.nauta.usecase.GetOrdersUseCase(orderPort, containerPort, invoicePort)
    }

    @Bean
    fun getContainersUseCase(
        containerPort: co.com.nauta.usecase.port.ContainerPort,
        orderPort: co.com.nauta.usecase.port.OrderPort
    ): co.com.nauta.usecase.GetContainersUseCase {
        return co.com.nauta.usecase.GetContainersUseCase(containerPort, orderPort)
    }
}
