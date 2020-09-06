package io.craigmiller160.authmanagementservice.integration.graphql.mutation

import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.dto.UserClientDto
import io.craigmiller160.authmanagementservice.dto.UserDto
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
class UserMutationIntegrationTest : AbstractGraphqlTest() {

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

    private lateinit var user1: User
    private lateinit var client1: Client
    private lateinit var client2: Client
    private lateinit var role1: Role
    private lateinit var role2: Role

    override fun getGraphqlBasePath(): String {
        return "graphql/mutation/user"
    }

    @BeforeEach
    fun setup() {
        user1 = userRepo.save(TestData.createUser(1))
        client1 = clientRepo.save(TestData.createClient(1))
        client2 = clientRepo.save(TestData.createClient(2))
        role1 = roleRepo.save(TestData.createRole(1, client1.id))
        role2 = roleRepo.save(TestData.createRole(2, client1.id))

        clientUserRepo.save(ClientUser(0, user1.id, client1.id))
        clientUserRoleRepo.save(ClientUserRole(0, client1.id, user1.id, role1.id))
    }

    @AfterEach
    fun clean() {
        clientUserRoleRepo.deleteAll()
        clientUserRepo.deleteAll()
        roleRepo.deleteAll()
        clientRepo.deleteAll()
        userRepo.deleteAll()
    }

    @Test
    fun `mutation - user - createUser`() {
        val result = execute("mutation_user_createUser", CreateUserResponse::class.java)

        val expected = UserDto(
                id = 2,
                email = "new@gmail.com",
                firstName = "New",
                lastName = "User",
                enabled = true,
                clients = listOf()
        )
        assertEquals(expected, result.createUser)

        val expectedDb = User(
                id = 2,
                email = "new@gmail.com",
                firstName = "New",
                lastName = "User",
                enabled = true,
                password = ""
        )
        val actualDb = userRepo.findById(2).get()
        assertEquals(expectedDb, actualDb.copy(password = ""))
        validateHash("NewPass", actualDb.password)
    }

    @Test
    fun `mutation - user - updateUser`() {
        val result = execute("mutation_user_updateUser", UpdateUserResponse::class.java)

        val expected = UserDto(
                id = 1,
                email = "update@gmail.com",
                firstName = "Updated",
                lastName = "User",
                enabled = true
        )
        assertEquals(expected, result.updateUser)

        val expectedDb = User(
                id = 1,
                email = "update@gmail.com",
                firstName = "Updated",
                lastName = "User",
                enabled = true,
                password = "password"
        )
        assertEquals(expectedDb, userRepo.findById(1).get())
    }

    @Test
    fun `mutation - user - updateUser with password`() {
        val result = execute("mutation_user_updateUserWithPassword", UpdateUserResponse::class.java)

        val expected = UserDto(
                id = 1,
                email = "update@gmail.com",
                firstName = "Updated",
                lastName = "User",
                enabled = true
        )
        assertEquals(expected, result.updateUser)

        val expectedDb = User(
                id = 1,
                email = "update@gmail.com",
                firstName = "Updated",
                lastName = "User",
                enabled = true,
                password = ""
        )
        val actualDb = userRepo.findById(1).get()
        assertEquals(expectedDb, actualDb.copy(password = ""))
        validateHash("NewPassword", actualDb.password)
    }

    @Test
    fun `mutation - user - deleteUser`() {
        val result = execute("mutation_user_deleteUser", DeleteUserResponse::class.java)

        val expected = UserDto.fromUser(user1)
        assertEquals(expected, result.deleteUser)

        assertEquals(0, userRepo.count())
        assertEquals(2, clientRepo.count())
        assertEquals(2, roleRepo.count())
        assertEquals(2, roleRepo.findAllByClientIdOrderByName(client1.id).size)
        assertEquals(0, clientRepo.findAllByUserOrderByName(user1.id).size)
        assertEquals(0, roleRepo.findAllByClientAndUserOrderByName(client1.id, user1.id).size)
    }

    @Test
    fun `mutation - user - removeClientFromUser`() {
        val result = execute("mutation_user_removeClientFromUser", RemoveClientFromUserResponse::class.java)

        assertEquals(listOf<UserClientDto>(), result.removeClientFromUser)

        assertEquals(1, userRepo.count())
        assertEquals(2, clientRepo.count())
        assertEquals(2, roleRepo.count())
        assertEquals(2, roleRepo.findAllByClientIdOrderByName(client1.id).size)
        assertEquals(0, clientRepo.findAllByUserOrderByName(user1.id).size)
        assertEquals(0, roleRepo.findAllByClientAndUserOrderByName(client1.id, user1.id).size)
    }

    @Test
    fun `mutation - user - addClientToUser`() {
        val result = execute("mutation_user_addClientToUser", AddClientToUserResponse::class.java)

        val clientDto1 = UserClientDto.fromClient(client1, 0)
        val clientDto2 = UserClientDto.fromClient(client2, 0)

        assertEquals(listOf(clientDto1, clientDto2), result.addClientToUser)

        assertEquals(1, userRepo.count())
        assertEquals(2, clientRepo.count())
        assertEquals(2, roleRepo.count())
        assertEquals(2, clientRepo.findAllByUserOrderByName(user1.id).size)
    }

    @Test
    fun `mutation - user - removeRoleFromUser`() {
        val result = execute("mutation_user_removeRoleFromUser", RemoveRoleFromUserResponse::class.java)

        assertEquals(listOf<RoleDto>(), result.removeRoleFromUser)

        assertEquals(1, userRepo.count())
        assertEquals(2, clientRepo.count())
        assertEquals(2, roleRepo.count())
        assertEquals(1, clientRepo.findAllByUserOrderByName(user1.id).size)
        assertEquals(0, roleRepo.findAllByClientAndUserOrderByName(client1.id, user1.id).size)
    }

    @Test
    fun `mutation - user - addRoleToUser`() {
        val result = execute("mutation_user_addRoleToUser", AddRoleToUserResponse::class.java)

        val roleDto1 = RoleDto.fromRole(role1)
        val roleDto2 = RoleDto.fromRole(role2)
        assertEquals(listOf(roleDto1, roleDto2), result.addRoleToUser)

        assertEquals(1, userRepo.count())
        assertEquals(2, clientRepo.count())
        assertEquals(2, roleRepo.count())
        assertEquals(1, clientRepo.findAllByUserOrderByName(user1.id).size)
        assertEquals(2, roleRepo.findAllByClientAndUserOrderByName(client1.id, user1.id).size)
    }

    class CreateUserResponse (
            val createUser: UserDto
    )

    class UpdateUserResponse (
            val updateUser: UserDto
    )

    class DeleteUserResponse (
            val deleteUser: UserDto
    )

    class RemoveClientFromUserResponse (
            val removeClientFromUser: List<UserClientDto>
    )

    class AddClientToUserResponse (
            val addClientToUser: List<UserClientDto>
    )

    class RemoveRoleFromUserResponse (
            val removeRoleFromUser: List<RoleDto>
    )

    class AddRoleToUserResponse (
            val addRoleToUser: List<RoleDto>
    )

}