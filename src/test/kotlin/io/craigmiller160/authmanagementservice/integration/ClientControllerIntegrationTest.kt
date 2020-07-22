package io.craigmiller160.authmanagementservice.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.nimbusds.jose.jwk.JWKSet
import io.craigmiller160.authmanagementservice.dto.FullClient
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.Role
import io.craigmiller160.authmanagementservice.entity.User
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.ClientUserRepository
import io.craigmiller160.authmanagementservice.repository.RoleRepository
import io.craigmiller160.authmanagementservice.repository.UserRepository
import io.craigmiller160.authmanagementservice.testutils.JwtUtils
import io.craigmiller160.authmanagementservice.testutils.TestData
import io.craigmiller160.authmanagementservice.testutils.integration.ApiProcessor
import io.craigmiller160.authmanagementservice.testutils.integration.UriArg
import io.craigmiller160.oauth2.config.OAuthConfig
import org.junit.jupiter.api.AfterEach
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.security.KeyPair
import java.util.*

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

    @Autowired
    private lateinit var userRepo: UserRepository

    @Autowired
    private lateinit var roleRepo: RoleRepository

    @Autowired
    private lateinit var clientUserRepo: ClientUserRepository

    private lateinit var token: String
    private lateinit var client1: Client
    private lateinit var client2: Client
    private lateinit var user1: User
    private lateinit var user2: User
    private lateinit var role1: Role
    private lateinit var role2: Role
    private lateinit var apiProcessor: ApiProcessor

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
        client1 = clientRepo.save(TestData.createClient(1))
        client2 = clientRepo.save(TestData.createClient(2))
        role1 = roleRepo.save(TestData.createRole(1, client1.id))
        role2 = roleRepo.save(TestData.createRole(2, client1.id))
        user1 = userRepo.save(TestData.createUser(1))
        user2 = userRepo.save(TestData.createUser(2))
        clientUserRepo.save(TestData.createClientUser(client1.id, user1.id))
        clientUserRepo.save(TestData.createClientUser(client1.id, user2.id))

        apiProcessor = ApiProcessor(
                mockMvc,
                objectMapper,
                isSecure = true,
                authToken = token
        )
    }

    @AfterEach
    fun clean() {
        clientRepo.deleteAll()
        userRepo.deleteAll()
        roleRepo.deleteAll()
    }

    @Test
    fun test_createClient() {
        val client = TestData.createClient(0)
        val result = apiProcessor.testPost("/clients", client)
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
    fun test_generateGuid() {
        val result = apiProcessor.testGet(UriArg("/clients/guid"))
        UUID.fromString(result.response.contentAsString)
    }

    @Test
    fun test_generateGuid_unauthorized() {
        mockMvc.perform(
                get("/clients/guid")
                        .secure(true)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized)
    }

    @Test
    fun test_getClient() {
        val result = apiProcessor.testGet(UriArg("/clients/{id}", listOf(client1.id)))
        val contentString = result.response.contentAsString
        val clientResult = objectMapper.readValue(contentString, FullClient::class.java)
        assertEquals(client1.copy(clientSecret = ""), clientResult.client)
        assertEquals(listOf(role1, role2), clientResult.roles.sortedBy { it.name })
        assertEquals(listOf(user1.copy(password = ""), user2.copy(password = "")), clientResult.users.sortedBy { it.email })
    }

    @Test
    fun test_getClient_noContent() {
        apiProcessor.testGet(UriArg("/clients/{id}", listOf(0)), 204)
    }

    @Test
    fun test_getClient_unauthorized() {
        mockMvc.perform(
                get("/clients/{id}", client1.id)
                        .secure(true)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized)
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
