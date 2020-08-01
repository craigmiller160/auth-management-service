package io.craigmiller160.authmanagementservice.integration

import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.Role
import io.craigmiller160.authmanagementservice.entity.User
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.ClientUserRepository
import io.craigmiller160.authmanagementservice.repository.RoleRepository
import io.craigmiller160.authmanagementservice.repository.UserRepository
import io.craigmiller160.authmanagementservice.testutils.TestData
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.UUID

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

}
