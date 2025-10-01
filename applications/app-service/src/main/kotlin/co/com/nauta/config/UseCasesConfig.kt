package co.com.nauta.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType

@Configuration
@ComponentScan(
    basePackages = ["co.com.nauta.usecase"],
    includeFilters = [
        ComponentScan.Filter(type = FilterType.REGEX, pattern = arrayOf("^.+UseCase$"))
    ],
    useDefaultFilters = false
)
class UseCasesConfig
