package io.craigmiller160.authmanagementservice.integration.graphql.query

import io.craigmiller160.authmanagementservice.dto.ClientDto
import io.craigmiller160.authmanagementservice.dto.ClientUserDto
import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.entity.*
import io.craigmiller160.authmanagementservice.integration.graphql.AbstractGraphqlTest
import io.craigmiller160.authmanagementservice.repository.*
import io.craigmiller160.authmanagementservice.testutils.TestData
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientQueryIntegrationTest : AbstractGraphqlTest() {

    @Autowired
    private lateinit var clientRepo: ClientRepository

    @Autowired
    private lateinit var userRepo: UserRepository

    @Autowired
    private lateinit var roleRepo: RoleRepository

    @Autowired
    private lateinit var clientUserRepo: ClientUserRepository

    @Autowired
    private lateinit var clientUserRoleRepo: ClientUserRoleRepository

    private lateinit var client1: Client
    private lateinit var client2: Client
    private lateinit var baseClient1Dto: ClientDto
    private lateinit var baseClient2Dto: ClientDto
    private lateinit var fullClient1Dto: ClientDto
    private lateinit var user1: User
    private lateinit var role1: Role
    private lateinit var role2: Role

    @BeforeEach
    fun setup() {
        client1 = clientRepo.save(TestData.createClient(1))
        client1 = clientRepo.save(
                client1.copy(clientRedirectUris = listOf(ClientRedirectUri(0, client1.id, "uri_1")))
        )
        client2 = clientRepo.save(TestData.createClient(2))
        user1 = userRepo.save(TestData.createUser(1))
        role1 = roleRepo.save(TestData.createRole(1, client1.id))
        role2 = roleRepo.save(TestData.createRole(2, client1.id))

        val clientUser = ClientUser(0, user1.id, client1.id)
        clientUserRepo.save(clientUser)

        val clientUserRole = ClientUserRole(0, client1.id, user1.id, role1.id)
        clientUserRoleRepo.save(clientUserRole)

        baseClient1Dto = ClientDto.fromClient(client1)
        baseClient2Dto = ClientDto.fromClient(client2)

        val clientUserDto = ClientUserDto.fromUser(user1, 0)
        val role1Dto = RoleDto.fromRole(role1)
        val role2Dto = RoleDto.fromRole(role2)
        fullClient1Dto = baseClient1Dto.copy(
                roles = listOf(role1Dto, role2Dto),
                users = listOf(clientUserDto.copy(roles = listOf(role1Dto)))
        )
    }

    @AfterEach
    fun clean() {
        clientUserRoleRepo.deleteAll()
        clientUserRepo.deleteAll()
        clientRepo.deleteAll()
        userRepo.deleteAll()
    }

    override fun getGraphqlBasePath(): String {
        return "graphql/query/client"
    }

    @Test
    fun `query - clients - base client only`() {
        val result = execute("query_clients_baseClientOnly", ClientsResponse::class.java)

        assertEquals(baseClient1Dto, result.clients[0])
        assertEquals(baseClient2Dto, result.clients[1])
    }

    @Test
    fun `query - single client - with roles and base users`() {
        val result = execute("query_singleClient_withRolesAndBaseUsers", ClientResponse::class.java)

        assertEquals(fullClient1Dto.copy(
                users = fullClient1Dto.users.map { it.copy(roles = listOf()) }
        ), result.client)
    }

    @Test
    fun `query - single client - with roles and users with roles`() {
        val result = execute("query_singleClient_withRolesAndUsersWithRoles", ClientResponse::class.java)

        assertEquals(fullClient1Dto, result.client)
    }

    @Test
    fun `query - single client - base client only`() {
        val result = execute("query_singleClient_baseClientOnly", ClientResponse::class.java)

        assertEquals(baseClient1Dto, result.client)
    }

    class ClientsResponse (
            val clients: List<ClientDto>
    )

    class ClientResponse (
            val client: ClientDto
    )

}
