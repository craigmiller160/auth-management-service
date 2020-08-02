package io.craigmiller160.authmanagementservice.integration

import io.craigmiller160.authmanagementservice.entity.RefreshToken
import io.craigmiller160.authmanagementservice.repository.RefreshTokenRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class UserControllerIntegrationTest : AbstractControllerIntegrationTest() {

    @Autowired
    private lateinit var refreshTokenRepo: RefreshTokenRepository

    private lateinit var userRefreshToken: RefreshToken
    private lateinit var clientRefreshToken: RefreshToken

    private val clientId = 1L
    private val userId = 2L
    private val userTokenId = "ABC"
    private val clientTokenId = "DEF"
    private val userToken = "GHI"
    private val clientToken = "JKL"

    @BeforeEach
    fun setup() {
        userRefreshToken = refreshTokenRepo.save(RefreshToken(userTokenId, userToken, clientId, userId, LocalDateTime.now()))
        clientRefreshToken = refreshTokenRepo.save(RefreshToken(clientTokenId, clientToken, clientId, null, LocalDateTime.now()))
    }

    @AfterEach
    fun clean() {
        refreshTokenRepo.deleteAll()
    }

    @Test
    fun test_getAuthDetails() {
        TODO("Finish this")
    }

    @Test
    fun test_getAuthDetails_unauthorized() {
        TODO("Finish this")
    }

    @Test
    fun test_clearAuthDetails() {
        TODO("Finish this")
    }

    @Test
    fun test_clearAuthDetails_unauthorized() {
        TODO("Finish this")
    }

}
