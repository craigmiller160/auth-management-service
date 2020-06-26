package io.craigmiller160.authmanagementservice.client

import io.craigmiller160.authmanagementservice.config.AuthServerConfig
import io.craigmiller160.authmanagementservice.dto.Jwk
import io.craigmiller160.authmanagementservice.dto.JwkList
import io.craigmiller160.authmanagementservice.exception.JwkLoadException
import org.springframework.web.client.RestTemplate

class AuthServerClient (
        private val restTemplate: RestTemplate,
        private val authServerConfig: AuthServerConfig
) {

}
