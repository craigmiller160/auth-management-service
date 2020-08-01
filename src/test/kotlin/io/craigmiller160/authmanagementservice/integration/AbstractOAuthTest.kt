package io.craigmiller160.authmanagementservice.integration

import com.nimbusds.jose.jwk.JWKSet
import io.craigmiller160.authmanagementservice.testutils.JwtUtils
import io.craigmiller160.oauth2.config.OAuthConfig
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.boot.test.mock.mockito.MockBean
import java.security.KeyPair

abstract class AbstractOAuthTest {

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

    lateinit var token: String

    @MockBean
    lateinit var oauthConfig: OAuthConfig

    @BeforeEach
    fun oauthSetup() {
        `when`(oauthConfig.jwkSet)
                .thenReturn(jwkSet)
        `when`(oauthConfig.clientKey)
                .thenReturn(JwtUtils.CLIENT_KEY)
        `when`(oauthConfig.clientName)
                .thenReturn(JwtUtils.CLIENT_NAME)

        val jwt = JwtUtils.createJwt()
        token = JwtUtils.signAndSerializeJwt(jwt, keyPair.private)
    }

}
