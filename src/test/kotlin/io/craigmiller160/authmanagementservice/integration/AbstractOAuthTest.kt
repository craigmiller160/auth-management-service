package io.craigmiller160.authmanagementservice.integration

import com.nimbusds.jose.jwk.JWKSet
import io.craigmiller160.authmanagementservice.testutils.JwtUtils
import io.craigmiller160.oauth2.config.OAuthConfig
import org.apache.catalina.filters.RestCsrfPreventionFilter
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.web.servlet.FilterRegistrationBean
import java.security.KeyPair

abstract class AbstractOAuthTest {

    companion object {

        protected lateinit var keyPair: KeyPair
        protected lateinit var jwkSet: JWKSet

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            keyPair = JwtUtils.createKeyPair()
            jwkSet = JwtUtils.createJwkSet(keyPair)
        }
    }

    protected lateinit var token: String

    @MockBean
    protected lateinit var oauthConfig: OAuthConfig

    @MockBean
    private lateinit var csrfFilter: FilterRegistrationBean<RestCsrfPreventionFilter>

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
