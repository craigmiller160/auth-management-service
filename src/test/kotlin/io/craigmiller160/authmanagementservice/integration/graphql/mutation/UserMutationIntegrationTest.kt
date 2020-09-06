package io.craigmiller160.authmanagementservice.integration.graphql.mutation

import io.craigmiller160.authmanagementservice.dto.UserDto
import io.craigmiller160.authmanagementservice.entity.User
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

    override fun getGraphqlBasePath(): String {
        return "graphql/mutation/user"
    }

    @BeforeEach
    fun setup() {
        user1 = userRepo.save(TestData.createUser(1))
    }

    @AfterEach
    fun clean() {
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
        TODO("Finish this")
    }

    @Test
    fun `mutation - user - updateUser with password`() {
        TODO("Finish this")
    }

    @Test
    fun `mutation - user - deleteUser`() {
        // TODO test breaking relationships
        TODO("Finish this")
    }

    @Test
    fun `mutation - user - removeClientFromUser`() {
        // TODO test user roles impact too
        TODO("Finish this")
    }

    @Test
    fun `mutation - user - addClientToUser`() {
        TODO("Finish this")
    }

    @Test
    fun `mutation - user - removeRoleFromUser`() {
        TODO("Finish this")
    }

    @Test
    fun `mutation - user - addRoleToUser`() {
        TODO("Finish this")
    }

    class CreateUserResponse (
            val createUser: UserDto
    )

}