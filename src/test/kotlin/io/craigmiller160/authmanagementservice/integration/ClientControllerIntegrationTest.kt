package io.craigmiller160.authmanagementservice.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.nimbusds.jose.jwk.JWKSet
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.testutils.JwtUtils
import io.craigmiller160.oauth2.config.OAuthConfig
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.security.KeyPair

@SpringBootTest
@ExtendWith(SpringExtension::class)
@AutoConfigureMockMvc
class ClientControllerIntegrationTest {

    companion object {

        private lateinit var keyPair: KeyPair
        private lateinit var jwkSet: JWKSet

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            keyPair = JwtUtils.createKeyPair()
            jwkSet = JwtUtils.createJwkSet(keyPair)
        }
    }

    @MockBean
    private lateinit var oauthConfig: OAuthConfig

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var token: String

    @BeforeEach
    fun setup() {
        `when`(oauthConfig.jwkSet)
                .thenReturn(jwkSet)
        `when`(oauthConfig.clientKey)
                .thenReturn(JwtUtils.CLIENT_KEY)
        `when`(oauthConfig.clientName)
                .thenReturn(JwtUtils.CLIENT_NAME)

        val jwt = JwtUtils.createJwt()
        token = JwtUtils.signAndSerializeJwt(jwt, keyPair.private)
    }

    @Test
    fun test_createClient() {
        val client = Client(
                id = 0,
                name = "Client",
                clientKey = "key",
                clientSecret = "secret",
                enabled = true,
                allowAuthCode = true,
                allowPassword = true,
                allowClientCredentials = true,
                accessTokenTimeoutSecs = 100,
                refreshTokenTimeoutSecs = 100
        )
        mockMvc.perform(
                post("/clients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(client))
                        .header("Authorization", "Bearer $token")
                        .secure(true)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
    }

}
