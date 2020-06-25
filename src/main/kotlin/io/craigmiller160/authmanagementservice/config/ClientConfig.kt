package io.craigmiller160.authmanagementservice.config

import io.craigmiller160.authmanagementservice.client.AuthServerClient
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class ClientConfig (
        private val authServerConfig: AuthServerConfig
) {

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplateBuilder().build()
    }

    @Bean
    fun authServerClient(restTemplate: RestTemplate): AuthServerClient {
        return AuthServerClient(restTemplate, authServerConfig)
    }

}