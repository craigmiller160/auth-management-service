package io.craigmiller160.authmanagementservice.security

import io.craigmiller160.authmanagementservice.client.AuthServerClient
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class TokenKeyService (
        private val authServerClient: AuthServerClient
) {

    @PostConstruct
    fun loadKey() {
        val jwk = authServerClient.getJwk()
        val key = jwk.n
    }

}