package io.craigmiller160.authmanagementservice.config

import io.craigmiller160.authmanagementservice.client.AuthServerClient
import io.craigmiller160.authmanagementservice.client.AuthServerClientImpl
import io.craigmiller160.authmanagementservice.client.RequestResponseLoggingInterceptor
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.client.BufferingClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.http.converter.FormHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

@Configuration
class ClientConfig (
        private val OAuthConfig: OAuthConfig
) {

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplateBuilder()
                .requestFactory { BufferingClientHttpRequestFactory(SimpleClientHttpRequestFactory()) }
                .interceptors(listOf(RequestResponseLoggingInterceptor()))
                .messageConverters(FormHttpMessageConverter(), MappingJackson2HttpMessageConverter())
                .build()
    }

    @Bean
    fun authServerClient(restTemplate: RestTemplate): AuthServerClient {
        return AuthServerClientImpl(restTemplate, OAuthConfig)
    }

}
