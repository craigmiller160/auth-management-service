package io.craigmiller160.authmanagementservice.integration.controller

import io.craigmiller160.apitestprocessor.config.AuthType
import io.craigmiller160.authmanagementservice.dto.ClientAuthDetailsDto
import io.craigmiller160.authmanagementservice.dto.OldClientAuthDetailsDto
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
import org.hamcrest.Matchers.*
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
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class ClientControllerIntegrationTest : AbstractControllerIntegrationTest() {

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

        userRefreshToken = refreshTokenRepo.save(RefreshToken(userTokenId, userToken, client.id, user.id, ZonedDateTime.now(ZoneId.of("UTC"))))
        clientRefreshToken = refreshTokenRepo.save(RefreshToken(clientTokenId, clientToken, client.id, null, ZonedDateTime.now(ZoneId.of("UTC"))))
    }

    @AfterEach
    fun clean() {
        clientUserRepo.deleteAll()
        clientRepo.deleteAll()
        userRepo.deleteAll()
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
    fun test_getClientAuthDetails() {
        val result = apiProcessor.call {
            request {
                path = "/clients/auth/${client.id}"
            }
            response {
                print = true
            }
        }.convert(ClientAuthDetailsDto::class.java)

        assertThat(result, allOf(
                hasProperty("clientName", equalTo(client.name)),
                hasProperty("userAuthDetails", containsInAnyOrder<UserAuthDetailsDto>(
                        allOf(
                                hasProperty("tokenId", equalTo(userRefreshToken.id)),
                                hasProperty("clientId", equalTo(client.id)),
                                hasProperty("clientName", equalTo(client.name)),
                                hasProperty("userId", equalTo(user.id)),
                                hasProperty("userEmail", equalTo(user.email)),
                                hasProperty("lastAuthenticated", notNullValue())
                        )
                ))
        ))
    }

    @Test
    fun test_getClientAuthDetails_clientNotExist() {
        apiProcessor.call {
            request {
                path = "/clients/auth/1000"
            }
            response {
                status = 400
            }
        }
    }

    @Test
    fun test_getClientAuthDetails_unauthorized() {
        apiProcessor.call {
            request {
                path = "/clients/auth/${client.id}"
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
