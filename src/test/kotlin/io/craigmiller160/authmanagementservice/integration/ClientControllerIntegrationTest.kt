package io.craigmiller160.authmanagementservice.integration

import io.craigmiller160.authmanagementservice.dto.ClientList
import io.craigmiller160.authmanagementservice.dto.FullClient
import io.craigmiller160.authmanagementservice.dto.FullClientList
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.Role
import io.craigmiller160.authmanagementservice.entity.User
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.ClientUserRepository
import io.craigmiller160.authmanagementservice.repository.RoleRepository
import io.craigmiller160.authmanagementservice.repository.UserRepository
import io.craigmiller160.authmanagementservice.testutils.JwtUtils
import io.craigmiller160.authmanagementservice.testutils.TestData
import io.craigmiller160.authmanagementservice.testutils.integration.ApiProcessor
import io.craigmiller160.authmanagementservice.testutils.integration.ApiProcessorBuilder
import io.craigmiller160.oauth2.config.OAuthConfig
import io.craigmiller160.webutils.dto.ErrorResponse
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpMethod
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.UUID

@SpringBootTest
@ExtendWith(SpringExtension::class)
class ClientControllerIntegrationTest : AbstractControllerIntegrationTest() {

    @Autowired
    private lateinit var clientRepo: ClientRepository

    @Autowired
    private lateinit var userRepo: UserRepository

    @Autowired
    private lateinit var roleRepo: RoleRepository

    @Autowired
    private lateinit var clientUserRepo: ClientUserRepository

    private lateinit var client1: Client
    private lateinit var client2: Client
    private lateinit var user1: User
    private lateinit var user2: User
    private lateinit var role1: Role
    private lateinit var role2: Role

    @BeforeEach
    fun setup() {
        client1 = clientRepo.save(TestData.createClient(1))
        client2 = clientRepo.save(TestData.createClient(2))
        role1 = roleRepo.save(TestData.createRole(1, client1.id))
        role2 = roleRepo.save(TestData.createRole(2, client1.id))
        user1 = userRepo.save(TestData.createUser(1))
        user2 = userRepo.save(TestData.createUser(2))
        clientUserRepo.save(TestData.createClientUser(client1.id, user1.id))
        clientUserRepo.save(TestData.createClientUser(client1.id, user2.id))
    }

    @AfterEach
    fun clean() {
        clientRepo.deleteAll()
        userRepo.deleteAll()
        roleRepo.deleteAll()
    }

    @Test
    fun test_createClient() {
        val client = TestData.createClient(0)
        val clientResult = apiProcessor.call {
            request {
                method = HttpMethod.POST
                path = "/clients"
                body = client
            }
        }.convert(Client::class.java)
        assertEquals(client.copy(id = clientResult.id, clientSecret = ""), clientResult)

        val dbClient = clientRepo.findById(clientResult.id).orElse(null)
        assertNotNull(dbClient)
        assertEquals(client.copy(id = clientResult.id), dbClient)
    }

    @Test
    fun test_createClient_unauthorized() {
        val client = TestData.createClient(0)
        apiProcessor.call {
            request {
                method = HttpMethod.POST
                path = "/clients"
                doAuth = false
                body = client
            }
            response {
                status = 401
            }
        }
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
    fun test_getClient() {
        val clientResult = apiProcessor.call {
            request {
                path = "/clients/${client1.id}"
            }
        }.convert(FullClient::class.java)
        assertEquals(client1.copy(clientSecret = ""), clientResult.client)
        assertEquals(listOf(role1, role2), clientResult.roles.sortedBy { it.name })
        assertEquals(listOf(user1.copy(password = ""), user2.copy(password = "")), clientResult.users.sortedBy { it.email })
    }

    @Test
    fun test_getClient_noContent() {
        apiProcessor.call {
            request {
                path = "/clients/0"
            }
            response {
                status = 204
            }
        }
    }

    @Test
    fun test_getClient_unauthorized() {
        apiProcessor.call {
            request {
                path = "/clients/1"
                doAuth = false
            }
            response {
                status = 401
            }
        }
    }

    @Test
    fun test_getClients() {
        val clientListResult = apiProcessor.call {
            request {
                path = "/clients"
            }
        }.convert(FullClientList::class.java)
        val clients = clientListResult.clients.sortedBy { it.client.name }

        assertEquals(client1.copy(clientSecret = ""), clients[0].client)
        assertTrue(clients[0].users.isEmpty())
        assertTrue(clients[0].roles.isEmpty())

        assertEquals(client2.copy(clientSecret = ""), clients[1].client)
        assertTrue(clients[1].users.isEmpty())
        assertTrue(clients[1].roles.isEmpty())
    }

    @Test
    fun test_getClients_full() {
        val clientListResult = apiProcessor.call {
            request {
                path = "/clients?full=true"
            }
        }.convert(FullClientList::class.java)
        val clients = clientListResult.clients.sortedBy { it.client.name }

        assertEquals(client1.copy(clientSecret = ""), clients[0].client)
        val client1Users = clients[0].users.sortedBy { it.email }
        assertEquals(user1.copy(password = ""), client1Users[0])
        assertEquals(user2.copy(password = ""), client1Users[1])
        val client1Roles = clients[0].roles.sortedBy { it.name }
        assertEquals(role1, client1Roles[0])
        assertEquals(role2, client1Roles[1])

        assertEquals(client2.copy(clientSecret = ""), clients[1].client)
        assertTrue(clients[1].users.isEmpty())
        assertTrue(clients[1].roles.isEmpty())
    }

    @Test
    fun test_getClients_noContent() {
        clean()
        apiProcessor.call {
            request {
                path = "/clients"
            }
            response {
                status = 204
            }
        }
    }

    @Test
    fun test_getClients_unauthorized() {
        apiProcessor.call {
            request {
                path = "/clients"
                doAuth = false
            }
            response {
                status = 401
            }
        }
    }

    @Test
    fun test_updateClient() {
        val newClient = client1.copy(
                name = "TotallyNewClient"
        )
        val clientResult = apiProcessor.call {
            request {
                method = HttpMethod.PUT
                path = "/clients/${client1.id}"
                body = newClient
            }
        }.convert(Client::class.java)

        assertEquals(newClient.copy(clientSecret = ""), clientResult)

        val dbClient = clientRepo.findById(newClient.id)
        assertEquals(newClient, dbClient.get())
    }

    @Test
    fun test_updateClient_noMatch() {
        val newClient = client1.copy(
                name = "TotallyNewClient"
        )
        val errorResult = apiProcessor.call {
            request {
                method = HttpMethod.PUT
                path = "/clients/0"
                body = newClient
            }
            response {
                status = 400
            }
        }.convert(ErrorResponse::class.java)

        assertEquals("Entity not found - Client not found for ID: 0", errorResult.message)
    }

    @Test
    fun test_updateClient_unauthorized() {
        val newClient = client1.copy(
                name = "TotallyNewClient"
        )
        apiProcessor.call {
            request {
                method = HttpMethod.PUT
                path = "/clients/0"
                body = newClient
                doAuth = false
            }
            response {
                status = 401
            }
        }
    }

    @Test
    fun test_deleteClient() {
        val clientResult = apiProcessor.call {
            request {
                method = HttpMethod.DELETE
                path = "/clients/${client1.id}"
            }
        }.convert(Client::class.java)
        assertEquals(client1.copy(clientSecret = ""), clientResult)

        val dbClient = clientRepo.findById(client1.id)
        assertTrue(dbClient.isEmpty)
    }

    @Test
    fun test_deleteClient_noMatch() {
        val errorResult = apiProcessor.call {
            request {
                method = HttpMethod.DELETE
                path = "/clients/0"
            }
            response {
                status = 400
            }
        }.convert(ErrorResponse::class.java)

        assertEquals("Entity not found - Client not found for ID: 0", errorResult.message)
    }

    @Test
    fun test_deleteClient_unauthorized() {
        apiProcessor.call {
            request {
                method = HttpMethod.DELETE
                path = "/clients/${client1.id}"
                doAuth = false
            }
            response {
                status = 401
            }
        }
    }

}
