package io.craigmiller160.authmanagementservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.craigmiller160.authmanagementservice.config.WebSecurityConfig
import io.craigmiller160.authmanagementservice.controller.advice.ClientListResponseAdvice
import io.craigmiller160.authmanagementservice.controller.advice.UserListResponseAdvice
import io.craigmiller160.authmanagementservice.dto.ClientList
import io.craigmiller160.authmanagementservice.dto.UserList
import io.craigmiller160.authmanagementservice.service.BasicService
import io.craigmiller160.authmanagementservice.testutils.JwtUtils
import io.craigmiller160.authmanagementservice.testutils.TestData
import io.craigmiller160.oauth2.client.AuthServerClient
import io.craigmiller160.oauth2.config.OAuthConfig
import io.craigmiller160.oauth2.dto.AuthUserDto
import io.craigmiller160.oauth2.repository.AppRefreshTokenRepository
import io.craigmiller160.oauth2.security.JwtValidationFilterConfigurer
import io.craigmiller160.oauth2.service.TokenRefreshService
import io.craigmiller160.webutils.security.AuthEntryPoint
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest
@ContextConfiguration(classes = [
    BasicController::class,
    WebSecurityConfig::class,
    AuthEntryPoint::class,
    UserListResponseAdvice::class,
    ClientListResponseAdvice::class,
    TokenRefreshService::class,
    JwtValidationFilterConfigurer::class // TODO need to reduce the number of lib classes that need to be listed here
])
class BasicControllerTest {

    @MockBean
    private lateinit var basicService: BasicService

    @MockBean
    private lateinit var oAuthConfig: OAuthConfig

    @MockBean
    private lateinit var appRefreshTokenRepo: AppRefreshTokenRepository

    @MockBean
    private lateinit var authServerClient: AuthServerClient

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var basicController: BasicController

    private val user = TestData.createUser()
    private val client = TestData.createClient()
    private lateinit var accessToken: String

    @BeforeEach
    fun setup() {
        val keyPair = JwtUtils.createKeyPair()
        val jwkSet = JwtUtils.createJwkSet(keyPair)
        `when`(oAuthConfig.jwkSet)
                .thenReturn(jwkSet)
        `when`(oAuthConfig.acceptBearerToken)
                .thenReturn(true)
        `when`(oAuthConfig.clientKey)
                .thenReturn(JwtUtils.CLIENT_KEY)
        `when`(oAuthConfig.clientName)
                .thenReturn(JwtUtils.CLIENT_NAME)

        val jwt = JwtUtils.createJwt()
        accessToken = JwtUtils.signAndSerializeJwt(jwt, keyPair.private)
    }

    @AfterEach
    fun clean() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun test_getUsers() {
        `when`(basicService.getUsers())
                .thenReturn(listOf(user))

        mockMvc.perform(
                MockMvcRequestBuilders.get("/basic/users")
                        .header("Authorization", "Bearer $accessToken")
                        .secure(true)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo { result ->
                    val userList = objectMapper.readValue(result.response.contentAsString, UserList::class.java)
                    assertEquals(1, userList.users.size)
                    assertEquals(user.copy(password = ""), userList.users[0])
                }
    }

    @Test
    fun test_getUsers_unauthorized() {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/basic/users")
                        .secure(true)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun test_getClients() {
        `when`(basicService.getClients())
                .thenReturn(listOf(client))

        mockMvc.perform(
                MockMvcRequestBuilders.get("/basic/clients")
                        .header("Authorization", "Bearer $accessToken")
                        .secure(true)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo { result ->
                    val clientList = objectMapper.readValue(result.response.contentAsString, ClientList::class.java)
                    assertEquals(1, clientList.clients.size)
                    assertEquals(client.copy(clientSecret = ""), clientList.clients[0])
                }
    }

    @Test
    fun test_getClients_unauthorized() {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/basic/clients")
                        .secure(true)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun test_getAuthenticatedUser() {
        val authUser = AuthUserDto.fromAuthenticatedUser(JwtUtils.createAuthUser())

        mockMvc.perform(
                MockMvcRequestBuilders.get("/basic/auth")
                        .header("Authorization", "Bearer $accessToken")
                        .secure(true)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo { result ->
                    val body = objectMapper.readValue(result.response.contentAsString, AuthUserDto::class.java)
                    assertEquals(authUser, body)
                }
    }

    @Test
    fun test_getAuthenticatedUser_unauthorized() {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/basic/auth")
                        .secure(true)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

}
