package io.craigmiller160.authmanagementservice.integration.controller

import io.craigmiller160.apitestprocessor.config.AuthType
import io.craigmiller160.authmanagementservice.dto.ClientAuthDetailsDto
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
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
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

    private lateinit var client1user1Expired: RefreshToken
    private lateinit var client1user1Valid: RefreshToken
    private lateinit var client2user1Valid: RefreshToken
    private lateinit var client1user2Valid: RefreshToken
    private lateinit var client1userNullValid: RefreshToken
    private lateinit var client1: Client
    private lateinit var client2: Client
    private lateinit var user1: User
    private lateinit var user2: User

    private val userTokenId = "ABC"
    private val clientTokenId = "DEF"
    private val userToken = "GHI"
    private val clientToken = "JKL"

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
                path = "/clients/auth/${client1.id}"
            }
            response {
                print = true
            }
        }.convert(ClientAuthDetailsDto::class.java)

        assertThat(result, allOf(
                hasProperty("clientName", equalTo(client1.name)),
                hasProperty("userAuthDetails", hasSize<List<UserAuthDetailsDto>>(1)),
                hasProperty("userAuthDetails", containsInAnyOrder<UserAuthDetailsDto>(
                        allOf(
                                hasProperty("clientId", equalTo(client1.id)),
                                hasProperty("clientName", equalTo(client1.name)),
                                hasProperty("userId", equalTo(user1.id)),
                                hasProperty("userEmail", equalTo(user1.email)),
                                hasProperty("lastAuthenticated", notNullValue())
                        )
                ))
        ))
        TODO("Update to validate timestamps")
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
                path = "/clients/auth/${client1.id}"
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
