package io.craigmiller160.authmanagementservice.integration.graphql.query

import io.craigmiller160.authmanagementservice.repository.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserQueryIntegrationTest {

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

    @BeforeEach
    fun setup() {

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
    fun `query - users - base user only`() {
        TODO("Finish this")
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

}