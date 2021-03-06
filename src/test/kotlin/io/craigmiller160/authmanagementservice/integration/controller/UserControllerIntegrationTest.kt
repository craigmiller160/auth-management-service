package io.craigmiller160.authmanagementservice.integration.controller

import io.craigmiller160.apitestprocessor.config.AuthType
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
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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

    private lateinit var client1user1Expired: RefreshToken
    private lateinit var client1user1Valid: RefreshToken
    private lateinit var client1user1ValidB: RefreshToken
    private lateinit var client2user1Valid: RefreshToken
    private lateinit var client1user2Valid: RefreshToken
    private lateinit var client1userNullValid: RefreshToken
    private lateinit var client1: Client
    private lateinit var client2: Client
    private lateinit var user1: User
    private lateinit var user2: User

    private val EXPIRED_TIMESTAMP: ZonedDateTime = ZonedDateTime.now().minusHours(1)
    private val TIMESTAMP: ZonedDateTime = ZonedDateTime.now()
    private val FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    @BeforeEach
    fun setup() {
        client1 = clientRepo.save(TestData.createClient(1))
        client2 = clientRepo.save(TestData.createClient(2))
        user1 = userRepo.save(TestData.createUser(1))
        user2 = userRepo.save(TestData.createUser(2))
        clientUserRepo.save(ClientUser(0, user1.id, client1.id))
        clientUserRepo.save(ClientUser(0, user1.id, client2.id))

        client1user1Expired = refreshTokenRepo.save(RefreshToken("Id1", "Token1", client1.id, user1.id, EXPIRED_TIMESTAMP))
        client1user1Valid = refreshTokenRepo.save(RefreshToken("Id2", "Token2", client1.id, user1.id, TIMESTAMP))
        client2user1Valid = refreshTokenRepo.save(RefreshToken("Id3", "Token3", client2.id, user1.id, TIMESTAMP))
        client1user2Valid = refreshTokenRepo.save(RefreshToken("Id4", "Token4", client1.id, user2.id, TIMESTAMP))
        client1userNullValid = refreshTokenRepo.save(RefreshToken("Id5", "Token5", client1.id, null, TIMESTAMP))
        client1user1ValidB = refreshTokenRepo.save(RefreshToken("Id6", "Token6", client1.id, user1.id, TIMESTAMP.plusMinutes(5)))
    }

    @AfterEach
    fun clean() {
        refreshTokenRepo.deleteAll()
        clientUserRepo.deleteAll()
        userRepo.deleteAll()
        clientRepo.deleteAll()
    }

    private fun validateToken(expected: RefreshToken, actual: RefreshToken) {
        assertThat(actual, allOf(
                hasProperty("id", equalTo(expected.id)),
                hasProperty("refreshToken", equalTo(expected.refreshToken))
        ))
    }

    @Test
    fun test_revokeUserAuthAccess() {
        apiProcessor.call {
            request {
                method = HttpMethod.POST
                path = "/users/auth/${user1.id}/${client1.id}/revoke"
            }
            response {
                status = 204
            }
        }

        val tokens = refreshTokenRepo.findAll()
        val sortedTokens = tokens.sortedBy { it.id }
        assertEquals(3, sortedTokens.size)
        validateToken(client2user1Valid, sortedTokens[0])
        validateToken(client1user2Valid, sortedTokens[1])
        validateToken(client1userNullValid, sortedTokens[2])
    }

    @Test
    fun test_revokeUserAuthAccess_userNotExists() {
        apiProcessor.call {
            request {
                method = HttpMethod.POST
                path = "/users/auth/${user1.id}/1000/revoke"
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
                path = "/users/auth/${user1.id}/${client1.id}/revoke"
                overrideAuth {
                    type = AuthType.NONE
                }
            }
            response {
                status = 401
            }
        }
    }

    private fun format(timestamp: ZonedDateTime): String =
            FORMAT.format(timestamp.withZoneSameInstant(ZoneId.of("UTC")))

    @Test
    fun test_getAllUserAuthDetails() {
        val result = apiProcessor.call {
            request {
                path = "/users/auth/${user1.id}"
            }
        }.convert(UserAuthDetailsListDto::class.java)

        val authDetails = result.authDetails
        val sortedAuthDetails = authDetails.sortedBy { it.clientId }
        assertEquals(2, authDetails.size)
        assertThat(sortedAuthDetails, allOf(
                hasSize(2),
                containsInAnyOrder(
                        allOf(
                                hasProperty("clientId", equalTo(client1.id)),
                                hasProperty("userId", equalTo(user1.id)),
                                hasProperty("lastAuthenticated", notNullValue()),
                                hasProperty("clientName", equalTo(client1.name)),
                                hasProperty("userEmail", equalTo(user1.email))
                        ),
                        allOf(
                                hasProperty("clientId", equalTo(client2.id)),
                                hasProperty("userId", equalTo(user1.id)),
                                hasProperty("lastAuthenticated", notNullValue()),
                                hasProperty("clientName", equalTo(client2.name)),
                                hasProperty("userEmail", equalTo(user1.email))
                        )
                )
        ))
        assertEquals(format(client1user1ValidB.timestamp), format(sortedAuthDetails[0].lastAuthenticated))
        assertEquals(format(client2user1Valid.timestamp), format(sortedAuthDetails[1].lastAuthenticated))
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
                path = "/users/auth/${user1.id}"
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
