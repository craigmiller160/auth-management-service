package io.craigmiller160.authmanagementservice.integration

import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.Role
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.RoleRepository
import io.craigmiller160.authmanagementservice.testutils.TestData
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
class ClientRoleControllerIntegrationTest : AbstractControllerIntegrationTest() {

    @Autowired
    private lateinit var clientRepo: ClientRepository
    @Autowired
    private lateinit var roleRepo: RoleRepository

    private lateinit var client1: Client
    private lateinit var role1: Role
    private lateinit var role2: Role

    @BeforeEach
    fun setup() {
        client1 = clientRepo.save(TestData.createClient(1))
        role1 = roleRepo.save(TestData.createRole(1, client1.id))
        role2 = roleRepo.save(TestData.createRole(2, client1.id))
    }

    @AfterEach
    fun clean() {

    }

    @Test
    fun test_getRoles() {
        apiProcessor.call {
            request {
                path = "/clients/${client1.id}/roles"
            }
        }
    }

    @Test
    fun test_getRoles_noContent() {
        TODO("Finish this")
    }

    @Test
    fun test_getRoles_unauthorized() {
        TODO("Finish this")
    }

    @Test
    fun test_createRole() {
        TODO("Finish this")
    }

    @Test
    fun test_createRole_unauthorized() {
        TODO("Finish this")
    }

    @Test
    fun test_updateRole() {
        TODO("Finish this")
    }

    @Test
    fun test_updateRole_noMatch() {
        TODO("Finish this")
    }

    @Test
    fun test_updateRole_unauthorized() {
        TODO("Finish this")
    }

    @Test
    fun test_deleteRole() {
        TODO("Finish this")
    }

    @Test
    fun test_deleteRole_noMatch() {
        TODO("Finish this")
    }

    @Test
    fun test_deleteRole_unauthorized() {
        TODO("Finish this")
    }

}
