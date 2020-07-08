package io.craigmiller160.authmanagementservice.config

import io.craigmiller160.authmanagementservice.client.RequestResponseLoggingInterceptor
import io.craigmiller160.webutils.oauth2.AuthServerClient
import io.craigmiller160.webutils.oauth2.AuthServerClientImpl
import io.craigmiller160.webutils.oauth2.OAuthConfig
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.BufferingClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.http.converter.FormHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

@Configuration
class ClientConfig (
        private val oAuthConfig: OAuthConfig
) {

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplateBuilder() // TODO move this to lib
                .requestFactory { BufferingClientHttpRequestFactory(SimpleClientHttpRequestFactory()) }
                .interceptors(listOf(RequestResponseLoggingInterceptor()))
                .messageConverters(FormHttpMessageConverter(), MappingJackson2HttpMessageConverter())
                .build()
    }

    @Bean
    fun authServerClient(restTemplate: RestTemplate): AuthServerClient {
        return AuthServerClientImpl(restTemplate, oAuthConfig)
    }

}
