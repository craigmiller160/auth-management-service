package io.craigmiller160.authmanagementservice.integration

import com.nimbusds.jose.jwk.JWKSet
import io.craigmiller160.authmanagementservice.testutils.JwtUtils
import org.junit.jupiter.api.BeforeAll
import java.security.KeyPair

abstract class AbstractControllerIntegrationTest {

    companion object {

        lateinit var keyPair: KeyPair
        lateinit var jwkSet: JWKSet

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            keyPair = JwtUtils.createKeyPair()
            jwkSet = JwtUtils.createJwkSet(keyPair)
        }
    }

}
