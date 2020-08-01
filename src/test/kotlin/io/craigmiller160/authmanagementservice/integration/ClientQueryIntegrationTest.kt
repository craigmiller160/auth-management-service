package io.craigmiller160.authmanagementservice.integration

import com.nimbusds.jose.jwk.JWKSet
import io.craigmiller160.authmanagementservice.testutils.JwtUtils
import io.craigmiller160.oauth2.config.OAuthConfig
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.security.KeyPair

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientQueryIntegrationTest {

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

    @BeforeEach
    fun setup() {
        Mockito.`when`(oauthConfig.jwkSet)
                .thenReturn(jwkSet)
        Mockito.`when`(oauthConfig.clientKey)
                .thenReturn(JwtUtils.CLIENT_KEY)
        Mockito.`when`(oauthConfig.clientName)
                .thenReturn(JwtUtils.CLIENT_NAME)
    }

    @Test
    fun test() {
        println("Working")
    }

}
