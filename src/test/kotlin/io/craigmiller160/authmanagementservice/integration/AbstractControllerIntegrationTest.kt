package io.craigmiller160.authmanagementservice.integration

import com.nimbusds.jose.jwk.JWKSet
import io.craigmiller160.authmanagementservice.testutils.JwtUtils
import io.craigmiller160.authmanagementservice.testutils.integration.ApiProcessor
import io.craigmiller160.authmanagementservice.testutils.integration.ApiProcessorBuilder
import io.craigmiller160.oauth2.config.OAuthConfig
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.mock.mockito.MockBean
import java.security.KeyPair

@AutoConfigureMockMvc
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

    @MockBean
    lateinit var oauthConfig: OAuthConfig

    lateinit var token: String
    lateinit var apiProcessor: ApiProcessor

    @Autowired
    private lateinit var apiProcessBuilder: ApiProcessorBuilder

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

        apiProcessor = apiProcessBuilder.build(
                https = true,
                authToken = token
        )
    }

}
