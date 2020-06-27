package io.craigmiller160.authmanagementservice.client

import io.craigmiller160.authmanagementservice.config.OAuthConfig
import org.springframework.web.client.RestTemplate

class AuthServerClient (
        private val restTemplate: RestTemplate,
        private val OAuthConfig: OAuthConfig
) {

}
