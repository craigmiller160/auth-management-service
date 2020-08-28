package io.craigmiller160.authmanagementservice.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class ClientConfig {

    // TODO try to eliminate this global overridden rest template... or work with it better, not sure. see if tests will work now
    @Bean
    fun restTemplate(authRestTemplateBuilder: RestTemplateBuilder): RestTemplate {
        return authRestTemplateBuilder.build()
    }

}
