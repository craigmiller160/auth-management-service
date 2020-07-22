package io.craigmiller160.authmanagementservice.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.nimbusds.jose.jwk.JWKSet
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.testutils.JwtUtils
import io.craigmiller160.authmanagementservice.testutils.TestData
import io.craigmiller160.oauth2.config.OAuthConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
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

    @Autowired
    private lateinit var clientRepo: ClientRepository

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
        val client = TestData.createClient(0)
        val result = mockMvc.perform(
                post("/clients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(client))
                        .header("Authorization", "Bearer $token")
                        .secure(true)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk)
                .andReturn()
        val content = result.response.contentAsString
        val clientResult = objectMapper.readValue(content, Client::class.java)
        assertEquals(client.copy(id = clientResult.id, clientSecret = ""), clientResult)

        val dbClient = clientRepo.findById(clientResult.id).orElse(null)
        assertNotNull(dbClient)
        assertEquals(client.copy(id = clientResult.id), dbClient)
    }

    @Test
    fun test_createClient_unauthorized() {
        val client = TestData.createClient(0)
        mockMvc.perform(
                post("/clients")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(client))
                        .secure(true)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized)
    }

    @Test
    fun test_getClient() {
        TODO("Finish this")
    }

    @Test
    fun test_getClient_noContent() {
        TODO("Finish this")
    }

    @Test
    fun test_getClient_unauthorized() {
        TODO("Finish this")
    }

    @Test
    fun test_getClients() {
        TODO("Finish this")
    }

    @Test
    fun test_getClients_noContent() {
        TODO("Finish this")
    }

    @Test
    fun test_getClients_unauthorized() {
        TODO("Finish this")
    }

    @Test
    fun test_updateClient() {
        TODO("Finish this")
    }

    @Test
    fun test_updateClient_noMatch() {
        TODO("Finish this")
    }

    @Test
    fun test_updateClient_unauthorized() {
        TODO("Finish this")
    }

    @Test
    fun test_deleteClient() {
        TODO("Finish this")
    }

    @Test
    fun test_deleteClient_noMatch() {
        TODO("Finish this")
    }

    @Test
    fun test_deeClient_unauthorized() {
        TODO("Finish this")
    }

    // TODO need role API tests

}
