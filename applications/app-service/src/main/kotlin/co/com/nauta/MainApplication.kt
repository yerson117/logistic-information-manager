package co.com.nauta

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan

@SpringBootApplication
@ConfigurationPropertiesScan
class MainApplication

fun main(args: Array<String>) {
    SpringApplication.run(MainApplication::class.java, *args)
}
