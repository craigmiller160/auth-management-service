package io.craigmiller160.authmanagementservice.integration.graphql.mutation

import io.craigmiller160.authmanagementservice.dto.ClientDto
import io.craigmiller160.authmanagementservice.dto.ClientUserDto
import io.craigmiller160.authmanagementservice.entity.*
import io.craigmiller160.authmanagementservice.integration.graphql.AbstractGraphqlTest
import io.craigmiller160.authmanagementservice.repository.*
import io.craigmiller160.authmanagementservice.testutils.TestData
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientMutationIntegrationTest : AbstractGraphqlTest() {

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
    private lateinit var role1: Role
    private lateinit var user1: User
    private lateinit var user2: User

    override fun getGraphqlBasePath(): String {
        return "graphql/mutation/client"
    }

    @BeforeEach
    fun setup() {
        client1 = clientRepo.save(TestData.createClient(1))
        role1 = roleRepo.save(TestData.createRole(1, client1.id))
        user1 = userRepo.save(TestData.createUser(1))
        user2 = userRepo.save(TestData.createUser(2))

        clientUserRepo.save(ClientUser(0, user1.id, client1.id))
        clientUserRoleRepo.save(ClientUserRole(0, client1.id, user1.id, role1.id))
    }

    @AfterEach
    fun clean() {
        clientUserRoleRepo.deleteAll()
        clientUserRepo.deleteAll()
        userRepo.deleteAll()
        clientRepo.deleteAll()
    }

    @Test
    fun `mutation - client - createClient`() {
        val result = execute("mutation_client_createClient", CreateClientResponse::class.java)

        val expected = ClientDto(
                id = 2,
                name = "New Client",
                clientKey = "NewKey",
                enabled = true,
                accessTokenTimeoutSecs = 100,
                refreshTokenTimeoutSecs = 200,
                authCodeTimeoutSecs = 10
        )
        assertEquals(expected, result.createClient)

        val expectedDb = Client(
                id = 2,
                name = "New Client",
                clientKey = "NewKey",
                clientSecret = "",
                enabled = true,
                accessTokenTimeoutSecs = 100,
                refreshTokenTimeoutSecs = 200,
                authCodeTimeoutSecs = 10,
                clientRedirectUris = listOf()
        )
        val actualDb = clientRepo.findById(2).get()
        assertEquals(expectedDb, actualDb.copy(clientSecret = ""))
        validateHash("NewSecret", actualDb.clientSecret)
    }

    @Test
    fun `mutation - client - updateClient`() {
        val result = execute("mutation_client_updateClient", UpdateClientResponse::class.java)

        val expected = ClientDto(
                id = 1,
                name = "Updated Client",
                clientKey = "UpdateKey",
                enabled = true,
                accessTokenTimeoutSecs = 300,
                refreshTokenTimeoutSecs = 400,
                authCodeTimeoutSecs = 20
        )
        assertEquals(expected, result.updateClient)

        val expectedDb = Client(
                id = client1.id,
                name = "Updated Client",
                clientKey = "UpdateKey",
                clientSecret = "Secret_1",
                enabled = true,
                accessTokenTimeoutSecs = 300,
                refreshTokenTimeoutSecs = 400,
                authCodeTimeoutSecs = 20,
                clientRedirectUris = listOf()
        )
        assertEquals(expectedDb, clientRepo.findById(1).get())
    }

    @Test
    fun `mutation - client - updateClient with secret`() {
        val result = execute("mutation_client_updateClientWithSecret", UpdateClientResponse::class.java)

        val expected = ClientDto(
                id = 1,
                name = "Updated Client",
                clientKey = "UpdateKey",
                enabled = true,
                accessTokenTimeoutSecs = 300,
                refreshTokenTimeoutSecs = 400,
                authCodeTimeoutSecs = 20
        )
        assertEquals(expected, result.updateClient)

        val expectedDb = Client(
                id = client1.id,
                name = "Updated Client",
                clientKey = "UpdateKey",
                clientSecret = "",
                enabled = true,
                accessTokenTimeoutSecs = 300,
                refreshTokenTimeoutSecs = 400,
                authCodeTimeoutSecs = 20,
                clientRedirectUris = listOf()
        )
        val actualDb = clientRepo.findById(1).get()
        assertEquals(expectedDb, actualDb.copy(clientSecret = ""))
        validateHash("UpdateSecret", actualDb.clientSecret)
    }

    @Test
    fun `mutation - client - deleteClient`() {
        val result = execute("mutation_client_deleteClient", DeleteClientResponse::class.java)

        val expected = ClientDto.fromClient(client1)
        assertEquals(expected, result.deleteClient)

        assertEquals(0, clientRepo.count())
        assertEquals(0, roleRepo.count())
        assertEquals(2, userRepo.count())
        assertEquals(0, userRepo.findAllByClientIdOrderByEmail(client1.id).size)
    }

    @Test
    fun `mutation - client - removeUserFromClient`() {
        val result = execute("mutation_client_removeUserFromClient", RemoveUserFromClientResponse::class.java)

        assertEquals(listOf<ClientUserDto>(), result.removeUserFromClient)

        assertEquals(1, clientRepo.count())
        assertEquals(2, userRepo.count())
        assertEquals(1, roleRepo.count())
        assertEquals(0, userRepo.findAllByClientIdOrderByEmail(client1.id).size)
        assertEquals(0, clientRepo.findAllByUserOrderByName(user1.id).size)
        assertEquals(0, roleRepo.findAllByClientAndUserOrderByName(client1.id, user1.id).size)
    }

    @Test
    fun `mutation - client - addUserToClient`() {
        val result = execute("mutation_client_addUserToClient", AddUserToClientResponse::class.java)

        val expected1 = ClientUserDto.fromUser(user1, 0)
        val expected2 = ClientUserDto.fromUser(user2, 0)
        assertEquals(listOf(expected1, expected2), result.addUserToClient)

        assertEquals(1, clientRepo.count())
        assertEquals(2, userRepo.count())
        assertEquals(1, roleRepo.count())
        assertEquals(2, userRepo.findAllByClientIdOrderByEmail(client1.id).size)
        assertEquals(1, clientRepo.findAllByUserOrderByName(user2.id).size)
        assertEquals(0, roleRepo.findAllByClientAndUserOrderByName(client1.id, user2.id).size)
    }

    class CreateClientResponse (
            val createClient: ClientDto
    )

    class UpdateClientResponse (
            val updateClient: ClientDto
    )

    class DeleteClientResponse (
            val deleteClient: ClientDto
    )

    class RemoveUserFromClientResponse (
            val removeUserFromClient: List<ClientUserDto>
    )

    class AddUserToClientResponse (
            val addUserToClient: List<ClientUserDto>
    )

}