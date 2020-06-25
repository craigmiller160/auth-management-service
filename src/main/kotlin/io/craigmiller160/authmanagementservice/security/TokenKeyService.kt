package io.craigmiller160.authmanagementservice.security

import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.JWKSet
import io.craigmiller160.authmanagementservice.client.AuthServerClient
import org.springframework.stereotype.Service
import java.net.URL
import javax.annotation.PostConstruct

@Service
class TokenKeyService (
        private val authServerClient: AuthServerClient // TODO remove this
) {

    lateinit var jwk: JWK

    @PostConstruct
    fun loadKey() {
        // TODO add error handling here... and make it configurable
        val jwkSet = JWKSet.load(URL("https://localhost:7003/jwk"))
        jwk = jwkSet.getKeyByKeyId("oauth-jwt")
    }

}