package io.craigmiller160.authmanagementservice.client

import io.craigmiller160.authmanagementservice.config.AuthServerConfig
import io.craigmiller160.authmanagementservice.dto.Jwk
import io.craigmiller160.authmanagementservice.dto.JwkList
import org.springframework.web.client.RestTemplate

class AuthServerClient (
        private val restTemplate: RestTemplate,
        private val authServerConfig: AuthServerConfig
) {

    fun getJwk(): Jwk {
        val response = restTemplate.getForEntity("${authServerConfig.host}${authServerConfig.jwkPath}", JwkList::class.java)
        val jwkList: JwkList = response.body ?: throw Exception("")
        return jwkList.keys[0]
    }

}