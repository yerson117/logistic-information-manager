package co.com.nauta

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["co.com.nauta.*"])
@ConfigurationPropertiesScan
class MainApplication

fun main(args: Array<String>) {
    runApplication<MainApplication>(*args)
}
