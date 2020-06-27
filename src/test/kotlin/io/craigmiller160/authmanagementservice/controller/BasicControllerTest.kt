package io.craigmiller160.authmanagementservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.craigmiller160.authmanagementservice.config.AuthServerConfig
import io.craigmiller160.authmanagementservice.dto.ClientList
import io.craigmiller160.authmanagementservice.dto.UserList
import io.craigmiller160.authmanagementservice.security.JwtFilterConfigurer
import io.craigmiller160.authmanagementservice.service.BasicService
import io.craigmiller160.authmanagementservice.testutils.JwtUtils
import io.craigmiller160.authmanagementservice.testutils.TestData
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
    JwtFilterConfigurer::class,
    BasicController::class
])
class BasicControllerTest {

    @MockBean
    private lateinit var basicService: BasicService

    @MockBean
    private lateinit var authServerConfig: AuthServerConfig

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
        `when`(authServerConfig.jwkSet)
                .thenReturn(jwkSet)

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
        TODO("Finish this")
    }

    @Test
    fun test_getClients() {
        `when`(basicService.getClients())
                .thenReturn(listOf(client))

        mockMvc.perform(MockMvcRequestBuilders.get("/basic/clients"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo { result ->
                    val clientList = objectMapper.readValue(result.response.contentAsString, ClientList::class.java)
                    assertEquals(1, clientList.clients.size)
                    assertEquals(client, clientList.clients[0])
                }
    }

    @Test
    fun test_getClients_unauthorized() {
        TODO("Finish this")
    }

}
