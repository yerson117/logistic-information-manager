package co.com.nauta.config

import co.com.nauta.usecase.AuthenticationUseCase
import co.com.nauta.usecase.CreateClientUseCase
import co.com.nauta.usecase.GetClientUseCase
import co.com.nauta.usecase.GetContainersUseCase
import co.com.nauta.usecase.GetOrdersUseCase
import co.com.nauta.usecase.ProcessBookingUseCase
import co.com.nauta.usecase.ProcessContainerUseCase
import co.com.nauta.usecase.ProcessEmailUseCase
import co.com.nauta.usecase.ProcessInvoiceUseCase
import co.com.nauta.usecase.ProcessOrderUseCase
import co.com.nauta.usecase.UpdateClientUseCase
import co.com.nauta.usecase.port.BookingPort
import co.com.nauta.usecase.port.ClientPort
import co.com.nauta.usecase.port.ContainerPort
import co.com.nauta.usecase.port.EncryptionPort
import co.com.nauta.usecase.port.InvoicePort
import co.com.nauta.usecase.port.OrderContainerPort
import co.com.nauta.usecase.port.OrderPort
import co.com.nauta.usecase.port.UserPort
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@RequiredArgsConstructor
class UseCasesConfig {

    @Bean
    fun processBookingUseCase(bookingPort: BookingPort): ProcessBookingUseCase {
        return ProcessBookingUseCase(bookingPort)
    }

    @Bean
    fun processContainerUseCase(
        containerPort: ContainerPort,
        orderContainerPort: OrderContainerPort
    ): ProcessContainerUseCase {
        return ProcessContainerUseCase(containerPort, orderContainerPort)
    }

    @Bean
    fun processOrderUseCase(
        orderPort: OrderPort,
        orderContainerPort: OrderContainerPort
    ): ProcessOrderUseCase {
        return ProcessOrderUseCase(orderPort, orderContainerPort)
    }

    @Bean
    fun processInvoiceUseCase(invoicePort: InvoicePort): ProcessInvoiceUseCase {
        return ProcessInvoiceUseCase(invoicePort)
    }

    @Bean
    fun processEmailUseCase(
        processBookingUseCase: ProcessBookingUseCase,
        processContainerUseCase: ProcessContainerUseCase,
        processOrderUseCase: ProcessOrderUseCase,
        processInvoiceUseCase: ProcessInvoiceUseCase,
        containerPort: ContainerPort,
        orderPort: OrderPort
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
        orderPort: OrderPort,
        containerPort: ContainerPort,
        invoicePort: InvoicePort
    ): GetOrdersUseCase {
        return GetOrdersUseCase(orderPort, containerPort, invoicePort)
    }

    @Bean
    fun getContainersUseCase(
        containerPort: ContainerPort,
        orderPort: OrderPort
    ): GetContainersUseCase {
        return GetContainersUseCase(containerPort, orderPort)
    }
    
    @Bean
    fun authenticationUseCase(
        userPort: UserPort,
        encryptionPort: EncryptionPort,
        clientPort: ClientPort
    ): AuthenticationUseCase {
        return AuthenticationUseCase(userPort, encryptionPort, clientPort)
    }

    @Bean
    fun createClientUseCase(
        clientPort: ClientPort
    ): CreateClientUseCase {
        return CreateClientUseCase(clientPort)
    }

    @Bean
    fun getClientUseCase(
        clientPort: ClientPort
    ): GetClientUseCase {
        return GetClientUseCase(clientPort)
    }

    @Bean
    fun updateClientUseCase(
        clientPort: ClientPort
    ): UpdateClientUseCase {
        return UpdateClientUseCase(clientPort)
    }
}
