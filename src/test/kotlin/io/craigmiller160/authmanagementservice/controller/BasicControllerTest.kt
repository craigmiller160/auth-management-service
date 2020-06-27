package io.craigmiller160.authmanagementservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.craigmiller160.authmanagementservice.config.AuthServerConfig
import io.craigmiller160.authmanagementservice.dto.ClientList
import io.craigmiller160.authmanagementservice.dto.UserList
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.User
import io.craigmiller160.authmanagementservice.security.JwtFilterConfigurer
import io.craigmiller160.authmanagementservice.service.BasicService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest
@ContextConfiguration(classes = [
    JwtFilterConfigurer::class,
    AuthServerConfig::class
])
@TestPropertySource(properties = [
    "authmanageservice.authserver.host=https://localhost:7003",
    "authmanageservice.authserver.jwk-path=/jwk"
])
class BasicControllerTest {

    @MockBean
    private lateinit var basicService: BasicService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var basicController: BasicController

    private val user = User(
            id = 0,
            email = "craig@gmail.com",
            firstName = "Craig",
            lastName = "Miller",
            password = "password"
    )

    private val client = Client(
            id = 0,
            name = "Client",
            clientKey = "Key",
            clientSecret = "Secret",
            enabled = true,
            allowClientCredentials = false,
            allowPassword = false,
            allowAuthCode = false,
            accessTokenTimeoutSecs = 0,
            refreshTokenTimeoutSecs = 0
    )

    @Test
    fun test_getUsers() {
        `when`(basicService.getUsers())
                .thenReturn(listOf(user))

        mockMvc.perform(MockMvcRequestBuilders.get("/basic/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andDo { result ->
                    val userList = objectMapper.readValue(result.response.contentAsString, UserList::class.java)
                    assertEquals(1, userList.users.size)
                    assertEquals(user.copy(password = ""), userList.users[0])
                }
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

}
