package io.craigmiller160.authmanagementservice.integration

import io.craigmiller160.authmanagementservice.dto.ClientAuthDetailsDto
import io.craigmiller160.authmanagementservice.entity.RefreshToken
import io.craigmiller160.authmanagementservice.repository.RefreshTokenRepository
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime
import java.util.UUID

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class ClientControllerIntegrationTest : AbstractControllerIntegrationTest() {

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
    fun test_generateGuid() {
        val result = apiProcessor.call {
            request {
                path = "/clients/guid"
            }
        }.content
        val uuid = UUID.fromString(result)
        assertNotNull(uuid)
    }

    @Test
    fun test_generateGuid_unauthorized() {
        apiProcessor.call {
            request {
                path = "/clients/guid"
                doAuth = false
            }
            response {
                status = 401
            }
        }
    }

    @Test
    fun test_getClientAuthDetails() {
        val result = apiProcessor.call {
            request {
                path = "/clients/auth/$clientId"
            }
        }.convert(ClientAuthDetailsDto::class.java)

        assertThat(result, allOf(
                hasProperty("tokenId", equalTo(clientTokenId)),
                hasProperty("clientId", equalTo(clientId)),
                hasProperty("lastAuthenticated", equalTo(clientRefreshToken.timestamp))
        ))
    }

    @Test
    fun test_getClientAuthDetails_unauthorized() {
        apiProcessor.call {
            request {
                path = "/clients/auth/$clientId"
                doAuth = false
            }
            response {
                status = 401
            }
        }
    }

    @Test
    fun test_clearClientAuthDetails() {
        val result = apiProcessor.call {
            request {
                method = HttpMethod.POST
                path = "/clients/auth/$clientId/clear"
            }
        }.convert(ClientAuthDetailsDto::class.java)

        assertThat(result, allOf(
                hasProperty("tokenId", nullValue()),
                hasProperty("clientId", equalTo(clientId)),
                hasProperty("lastAuthenticated", nullValue())
        ))

        val tokens = refreshTokenRepo.findAll()
        assertEquals(1, tokens.size)
        assertEquals(userRefreshToken, tokens[0])
    }

    @Test
    fun test_clearClientAuthDetails_unauthorized() {
        apiProcessor.call {
            request {
                method = HttpMethod.POST
                path = "/clients/auth/$clientId/clear"
                doAuth = false
            }
            response {
                status = 401
            }
        }
    }

}
