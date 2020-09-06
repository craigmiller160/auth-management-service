package io.craigmiller160.authmanagementservice.integration.graphql.query

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
class UserQueryIntegrationTest : AbstractGraphqlTest() {

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
    private lateinit var user2: User
    private lateinit var client1: Client
    private lateinit var role1: Role
    private lateinit var role2: Role
    private lateinit var baseUser1Dto: UserDto
    private lateinit var baseUser2Dto: UserDto
    private lateinit var fullUser1Dto: UserDto

    @BeforeEach
    fun setup() {
        user1 = userRepo.save(TestData.createUser(1))
        user2 = userRepo.save(TestData.createUser(2))
        client1 = clientRepo.save(TestData.createClient(1))
        role1 = roleRepo.save(TestData.createRole(1, client1.id))
        role2 = roleRepo.save(TestData.createRole(2, client1.id))

        clientUserRoleRepo.save(ClientUserRole(0, client1.id, user1.id, role1.id))

        val clientUser = ClientUser(0, user1.id, client1.id)
        clientUserRepo.save(clientUser)

        baseUser1Dto = UserDto.fromUser(user1)
        baseUser2Dto = UserDto.fromUser(user2)

        val role1Dto = RoleDto.fromRole(role1)
        val role2Dto = RoleDto.fromRole(role2)
        val clientDto = UserClientDto.fromClient(client1, 0)
                .copy(allRoles = listOf(role1Dto, role2Dto), userRoles = listOf(role1Dto))
        fullUser1Dto = baseUser1Dto.copy(clients = listOf(clientDto))
    }

    @AfterEach
    fun clean() {
        clientUserRoleRepo.deleteAll()
        clientUserRepo.deleteAll()
        roleRepo.deleteAll()
        clientRepo.deleteAll()
        userRepo.deleteAll()
    }

    override fun getGraphqlBasePath(): String {
        return "graphql/query/user"
    }

    @Test
    fun `query - users - base user only`() {
        val result = execute("query_users_baseUserOnly", UsersResponse::class.java)

        assertEquals(baseUser1Dto, result.users[0])
        assertEquals(baseUser2Dto, result.users[1])
    }

    @Test
    fun `query - single user - base user only`() {
        val result = execute("query_singleUser_baseUserOnly", UserResponse::class.java)

        assertEquals(baseUser1Dto, result.user)
    }

    @Test
    fun `query - single user - with base clients`() {
        val result = execute("query_singleUser_withBaseClients", UserResponse::class.java)

        assertEquals(
                fullUser1Dto.copy(clients = fullUser1Dto.clients.map { it.copy(allRoles = listOf(), userRoles = listOf()) }),
                result.user
        )
    }

    @Test
    fun `query - single user - with clients and roles`() {
        val result = execute("query_singleUser_withClientsAndRoles", UserResponse::class.java)
        assertEquals(fullUser1Dto, result.user)
    }

    class UsersResponse (
            val users: List<UserDto>
    )

    class UserResponse (
            val user: UserDto
    )

}