package io.craigmiller160.authmanagementservice.integration

import io.craigmiller160.apitestprocessor.config.AuthType
import io.craigmiller160.authmanagementservice.dto.UserAuthDetailsDto
import io.craigmiller160.authmanagementservice.dto.UserAuthDetailsListDto
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
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.nullValue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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

    private lateinit var userRefreshToken1: RefreshToken
    private lateinit var userRefreshToken2: RefreshToken
    private lateinit var clientRefreshToken: RefreshToken
    private lateinit var client1: Client
    private lateinit var client2: Client
    private lateinit var user: User

    private val userToken1Id = "ABC"
    private val userToken2Id = "MNO"
    private val clientTokenId = "DEF"
    private val userToken1 = "GHI"
    private val userToken2 = "PQR"
    private val clientToken = "JKL"

    @BeforeEach
    fun setup() {
        client1 = clientRepo.save(TestData.createClient(1))
        client2 = clientRepo.save(TestData.createClient(2))
        user = userRepo.save(TestData.createUser(1))
        clientUserRepo.save(ClientUser(0, user.id, client1.id))
        clientUserRepo.save(ClientUser(0, user.id, client2.id))

        userRefreshToken1 = refreshTokenRepo.save(RefreshToken(userToken1Id, userToken1, client1.id, user.id, LocalDateTime.now()))
        userRefreshToken2 = refreshTokenRepo.save(RefreshToken(userToken2Id, userToken2, client2.id, user.id, LocalDateTime.now()))
        clientRefreshToken = refreshTokenRepo.save(RefreshToken(clientTokenId, clientToken, client1.id, null, LocalDateTime.now()))
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
                path = "/users/auth/${user.id}/${client1.id}"
            }
        }.convert(UserAuthDetailsDto::class.java)

        assertThat(result, allOf(
                hasProperty("tokenId", equalTo(userToken1Id)),
                hasProperty("clientId", equalTo(client1.id)),
                hasProperty("userId", equalTo(user.id)),
                hasProperty("lastAuthenticated", equalTo(userRefreshToken1.timestamp)),
                hasProperty("clientName", equalTo(client1.name)),
                hasProperty("userEmail", equalTo(user.email))
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
                path = "/users/auth/${user.id}/${client1.id}"
                overrideAuth {
                    type = AuthType.NONE
                }
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
                path = "/users/auth/${user.id}/${client1.id}/revoke"
            }
        }.convert(UserAuthDetailsDto::class.java)

        assertThat(result, allOf(
                hasProperty("tokenId", nullValue()),
                hasProperty("clientId", equalTo(client1.id)),
                hasProperty("userId", equalTo(user.id)),
                hasProperty("lastAuthenticated", nullValue()),
                hasProperty("clientName", equalTo(client1.name)),
                hasProperty("userEmail", equalTo(user.email))
        ))

        val tokens = refreshTokenRepo.findAll()
        assertEquals(2, tokens.size)
        assertTrue(tokens.contains(clientRefreshToken))
        assertTrue(tokens.contains(userRefreshToken2))
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
                path = "/users/auth/${user.id}/${client1.id}/revoke"
                overrideAuth {
                    type = AuthType.NONE
                }
            }
            response {
                status = 401
            }
        }
    }

    @Test
    fun test_getAllUserAuthDetails() {
        val result = apiProcessor.call {
            request {
                path = "/users/auth/${user.id}"
            }
        }.convert(UserAuthDetailsListDto::class.java)

        val authDetails = result.authDetails
        assertEquals(2, authDetails.size)
        assertThat(authDetails, allOf(
                hasSize(2),
                containsInAnyOrder(
                        allOf(
                                hasProperty("tokenId", equalTo(userToken1Id)),
                                hasProperty("clientId", equalTo(client1.id)),
                                hasProperty("userId", equalTo(user.id)),
                                hasProperty("lastAuthenticated", equalTo(userRefreshToken1.timestamp)),
                                hasProperty("clientName", equalTo(client1.name)),
                                hasProperty("userEmail", equalTo(user.email))
                        ),
                        allOf(
                                hasProperty("tokenId", equalTo(userToken2Id)),
                                hasProperty("clientId", equalTo(client2.id)),
                                hasProperty("userId", equalTo(user.id)),
                                hasProperty("lastAuthenticated", equalTo(userRefreshToken2.timestamp)),
                                hasProperty("clientName", equalTo(client2.name)),
                                hasProperty("userEmail", equalTo(user.email))
                        )
                )
        ))
    }

    @Test
    fun test_getAllUserAuthDetails_noUser() {
        apiProcessor.call {
            request {
                path = "/users/auth/1000"
            }
            response {
                status = 400
            }
        }
    }

    @Test
    fun test_getAllUserAuthDetails_unauthorized() {
        apiProcessor.call {
            request {
                path = "/users/auth/${user.id}"
                overrideAuth {
                    type = AuthType.NONE
                }
            }
            response {
                status = 401
            }
        }
    }

}
