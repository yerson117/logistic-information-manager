package co.com.nauta.config

import org.junit.jupiter.api.Test
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import kotlin.test.assertTrue

class UseCasesConfigTest {

    @Test
    fun testUseCaseBeansExist() {
        AnnotationConfigApplicationContext(TestConfig::class.java).use { context ->
            val beanNames = context.beanDefinitionNames

            var useCaseBeanFound = false
            for (beanName in beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true
                    break
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found")
        }
    }

    @Configuration
    @Import(UseCasesConfig::class)
    class TestConfig {

        @Bean
        fun myUseCase(): MyUseCase {
            return MyUseCase()
        }
    }

    class MyUseCase {
        fun execute(): String {
            return "MyUseCase Test"
        }
    }
}