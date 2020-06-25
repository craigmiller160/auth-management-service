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

    fun getJwk(): Jwk {
        try {
            val response = restTemplate.getForEntity("${authServerConfig.host}${authServerConfig.jwkPath}", JwkList::class.java)
            val jwkList: JwkList = response.body ?: throw Exception("")
            return jwkList.keys[0]
        } catch (ex: Exception) {
            throw JwkLoadException("Error loading JWK", ex)
        }
    }

}