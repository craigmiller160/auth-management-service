package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.User
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class BasicServiceTest {

    @Mock
    private lateinit var userRepo: UserRepository
    @Mock
    private lateinit var clientRepo: ClientRepository

    @InjectMocks
    private lateinit var basicService: BasicService

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
        val users = listOf(user)
        `when`(userRepo.findAll())
                .thenReturn(users)

        val result = basicService.getUsers()
        assertEquals(users, result)
    }

    @Test
    fun test_getClients() {
        val clients = listOf(client)
        `when`(clientRepo.findAll())
                .thenReturn(clients)

        val result = basicService.getClients()
        assertEquals(clients, result)
    }

}