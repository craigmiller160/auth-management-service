package io.craigmiller160.authmanagementservice.integration

import io.craigmiller160.authmanagementservice.dto.UserAuthDetailsDto
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.ClientUser
import io.craigmiller160.authmanagementservice.entity.RefreshToken
import io.craigmiller160.authmanagementservice.entity.User
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.ClientUserRepository
import io.craigmiller160.authmanagementservice.repository.RefreshTokenRepository
import io.craigmiller160.authmanagementservice.repository.UserRepository
import io.craigmiller160.authmanagementservice.testutils.TestData
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class UserControllerIntegrationTest : AbstractControllerIntegrationTest() {

    @Autowired
    private lateinit var refreshTokenRepo: RefreshTokenRepository

    @Autowired
    private lateinit var userRepo: UserRepository

    @Autowired
    private lateinit var clientRepo: ClientRepository

    @Autowired
    private lateinit var clientUserRepo: ClientUserRepository

    private lateinit var userRefreshToken: RefreshToken
    private lateinit var clientRefreshToken: RefreshToken
    private lateinit var client: Client
    private lateinit var user: User

    private val userTokenId = "ABC"
    private val clientTokenId = "DEF"
    private val userToken = "GHI"
    private val clientToken = "JKL"

    @BeforeEach
    fun setup() {
        client = clientRepo.save(TestData.createClient(1))
        user = userRepo.save(TestData.createUser(1))
        val clientUser = ClientUser(0, user.id, client.id)
        clientUserRepo.save(clientUser)

        userRefreshToken = refreshTokenRepo.save(RefreshToken(userTokenId, userToken, client.id, user.id, LocalDateTime.now()))
        clientRefreshToken = refreshTokenRepo.save(RefreshToken(clientTokenId, clientToken, client.id, null, LocalDateTime.now()))
    }

    @AfterEach
    fun clean() {
        refreshTokenRepo.deleteAll()
        clientUserRepo.deleteAll()
        userRepo.deleteAll()
        clientRepo.deleteAll()
    }

    @Test
    fun test_getAuthDetails() {
        val result = apiProcessor.call {
            request {
                path = "/users/auth/${user.id}/${client.id}"
            }
        }.convert(UserAuthDetailsDto::class.java)

        assertThat(result, allOf(
                hasProperty("tokenId", equalTo(userTokenId)),
                hasProperty("clientId", equalTo(client.id)),
                hasProperty("userId", equalTo(user.id)),
                hasProperty("lastAuthenticated", equalTo(userRefreshToken.timestamp))
        ))
    }

    @Test
    fun test_getAuthDetails_userNotExists() {
        apiProcessor.call {
            request {
                path = "/users/auth/${user.id}/1000"
            }
            response {
                status = 400
            }
        }
    }

    @Test
    fun test_getAuthDetails_unauthorized() {
        apiProcessor.call {
            request {
                path = "/users/auth/${user.id}/${client.id}"
                doAuth = false
            }
            response {
                status = 401
            }
        }
    }

    @Test
    fun test_revokeUserAuthAccess() {
        val result = apiProcessor.call {
            request {
                method = HttpMethod.POST
                path = "/users/auth/${user.id}/${client.id}/revoke"
            }
        }.convert(UserAuthDetailsDto::class.java)

        assertThat(result, allOf(
                hasProperty("tokenId", nullValue()),
                hasProperty("clientId", equalTo(client.id)),
                hasProperty("userId", equalTo(user.id)),
                hasProperty("lastAuthenticated", nullValue())
        ))

        val tokens = refreshTokenRepo.findAll()
        Assertions.assertEquals(1, tokens.size)
        Assertions.assertEquals(clientRefreshToken, tokens[0])
    }

    @Test
    fun test_revokeUserAuthAccess_userNotExists() {
        apiProcessor.call {
            request {
                method = HttpMethod.POST
                path = "/users/auth/${user.id}/1000/revoke"
            }
            response {
                status = 400
            }
        }
    }

    @Test
    fun test_revokeUserAuthAccess_unauthorized() {
        apiProcessor.call {
            request {
                method = HttpMethod.POST
                path = "/users/auth/${user.id}/${client.id}/revoke"
                doAuth = false
            }
            response {
                status = 401
            }
        }
    }

    @Test
    fun test_getAllUserAuthDetails() {
        TODO("Finish this")
    }

    @Test
    fun test_getAllUserAuthDetails_noUser() {
        TODO("Finish this")
    }

    @Test
    fun test_getAllUserAuthDetails_unauthorized() {
        TODO("Finish this")
    }

}
