package co.com.nauta.rest_api.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityRequirement
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Logistic Information Manager API")
                    .description("""
                        Sistema de gestión de información logística que permite el procesamiento progresivo 
                        de datos de bookings, contenedores, órdenes de compra e invoices, con capacidad de 
                        establecer relaciones entre entidades de forma incremental.
                        
                        ## Características Principales
                        - ✅ Procesamiento progresivo de datos
                        - ✅ Gestión de relaciones many-to-many
                        - ✅ Validaciones de negocio robustas
                        - ✅ Aislamiento de datos por cliente
                        - ✅ Autenticación JWT
                        
                        ## Flujo de Datos
                        1. Los datos llegan en diferentes momentos
                        2. Se procesan y validan automáticamente
                        3. Se establecen relaciones entre entidades
                        4. Se persisten con consistencia garantizada
                    """.trimIndent())
                    .version("1.0.0")
                    .contact(
                        Contact()
                            .name("Nauta Development Team")
                            .email("dev@nauta.com")
                    )
                    .license(
                        License()
                            .name("MIT License")
                            .url("https://opensource.org/licenses/MIT")
                    )
            )
            .components(
                Components()
                    .addSecuritySchemes(
                        "bearerAuth",
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .description("JWT token obtenido del endpoint /api/auth/login")
                    )
            )
            .addSecurityItem(
                SecurityRequirement()
                    .addList("bearerAuth")
            )
    }
}
