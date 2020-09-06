package io.craigmiller160.authmanagementservice.integration.graphql.query

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
    private lateinit var baseUser1Dto: UserDto
    private lateinit var baseUser2Dto: UserDto

    @BeforeEach
    fun setup() {
        user1 = userRepo.save(TestData.createUser(1))
        user2 = userRepo.save(TestData.createUser(2))

        baseUser1Dto = UserDto.fromUser(user1)
        baseUser2Dto = UserDto.fromUser(user2)
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
        TODO("Finish this")
    }

    @Test
    fun `query - single user - with base clients`() {
        TODO("Finish this")
    }

    @Test
    fun `query - single user - with clients and roles`() {
        TODO("Finish this")
    }

    class UsersResponse (
            val users: List<UserDto>
    )

}