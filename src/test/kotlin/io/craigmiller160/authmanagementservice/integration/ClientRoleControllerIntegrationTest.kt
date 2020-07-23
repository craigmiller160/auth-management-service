package io.craigmiller160.authmanagementservice.integration

import io.craigmiller160.authmanagementservice.dto.RoleList
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.Role
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.RoleRepository
import io.craigmiller160.authmanagementservice.testutils.TestData
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
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
        roleRepo.deleteAll()
        clientRepo.deleteAll()
    }

    @Test
    fun test_getRoles() {
        val roleListResult = apiProcessor.call {
            request {
                path = "/clients/{clientId}/roles"
                vars = arrayOf(client1.id)
            }
        }.convert(RoleList::class.java)

        val roles = roleListResult.roles.sortedBy { it.name }
        assertEquals(role1, roles[0])
        assertEquals(role2, roles[1])
    }

    @Test
    fun test_getRoles_noContent() {
        clean()
        apiProcessor.call {
            request {
                path = "/clients/{clientId}/roles"
                vars = arrayOf(client1.id)
            }
            response {
                status = 204
            }
        }
    }

    @Test
    fun test_getRoles_unauthorized() {
        apiProcessor.call {
            request {
                path = "/clients/{clientId}/roles"
                vars = arrayOf(client1.id)
                doAuth = false
            }
            response {
                status = 401
            }
        }
    }

    @Test
    fun test_createRole() {
        val role = TestData.createRole(0, client1.id)
        val roleResult = apiProcessor.call {
            request {
                method = HttpMethod.POST
                path = "/clients/{clientId}/roles"
                vars = arrayOf(client1.id)
                body = role
            }
        }.convert(Role::class.java)

        assertEquals(role.copy(id = roleResult.id), roleResult)

        val dbRole = roleRepo.findById(roleResult.id)
        assertEquals(role.copy(id = roleResult.id), dbRole.get())
    }

    @Test
    fun test_createRole_unauthorized() {
        val role = TestData.createRole(0, client1.id)
        apiProcessor.call {
            request {
                method = HttpMethod.POST
                path = "/clients/{clientId}/roles"
                vars = arrayOf(client1.id)
                body = role
                doAuth = false
            }
            response {
                status = 401
            }
        }
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
