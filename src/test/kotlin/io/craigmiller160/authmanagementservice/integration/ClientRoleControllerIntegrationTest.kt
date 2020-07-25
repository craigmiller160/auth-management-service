package io.craigmiller160.authmanagementservice.integration

import io.craigmiller160.authmanagementservice.dto.RoleList
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.Role
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.RoleRepository
import io.craigmiller160.authmanagementservice.testutils.TestData
import io.craigmiller160.webutils.dto.ErrorResponse
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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
                path = "/clients/${client1.id}/roles"
            }
        }.convert(RoleList::class.java)

        assertEquals(2, roleListResult.roles.size)
        assertEquals(role1, roleListResult.roles[0])
        assertEquals(role2, roleListResult.roles[1])
    }

    @Test
    fun test_getRoles_noContent() {
        clean()
        apiProcessor.call {
            request {
                path = "/clients/${client1.id}/roles"
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
                path = "/clients/${client1.id}/roles"
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
                path = "/clients/${client1.id}/roles"
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
                path = "/clients/${client1.id}/roles"
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
        val role = role1.copy(name = "ABC")
        val roleResult = apiProcessor.call {
            request {
                method = HttpMethod.PUT
                path = "/clients/${client1.id}/roles/${role.id}"
                body = role
            }
        }.convert(Role::class.java)

        assertEquals(role, roleResult)

        val dbRole = roleRepo.findById(role.id)
        assertEquals(role, dbRole.get())
    }

    @Test
    fun test_updateRole_noMatch() {
        val role = TestData.createRole(0, client1.id)
        val errorResult = apiProcessor.call {
            request {
                method = HttpMethod.PUT
                path = "/clients/${client1.id}/roles/0"
                body = role
            }
            response {
                status = 400
            }
        }.convert(ErrorResponse::class.java)

        assertEquals("Entity not found - Role not found for ClientID ${client1.id} and RoleID 0", errorResult.message)
    }

    @Test
    fun test_updateRole_unauthorized() {
        val role = TestData.createRole(0, client1.id)
        apiProcessor.call {
            request {
                method = HttpMethod.PUT
                path = "/clients/${client1.id}/roles/0"
                body = role
                doAuth = false
            }
            response {
                status = 401
            }
        }
    }

    @Test
    fun test_deleteRole() {
        val roleResult = apiProcessor.call {
            request {
                method = HttpMethod.DELETE
                path = "/clients/${client1.id}/roles/${role1.id}"
            }
        }.convert(Role::class.java)

        assertEquals(role1, roleResult)

        val dbRole = roleRepo.findById(role1.id)
        assertTrue(dbRole.isEmpty)
    }

    @Test
    fun test_deleteRole_noMatch() {
        val errorResult = apiProcessor.call {
            request {
                method = HttpMethod.DELETE
                path = "/clients/${client1.id}/roles/0"
            }
            response {
                status = 400
            }
        }.convert(ErrorResponse::class.java)

        assertEquals("Entity not found - Role not found for ClientID ${client1.id} and RoleID 0", errorResult.message)
    }

    @Test
    fun test_deleteRole_unauthorized() {
        apiProcessor.call {
            request {
                method = HttpMethod.DELETE
                path = "/clients/${client1.id}/roles/${role1.id}"
                doAuth = false
            }
            response {
                status = 401
            }
        }
    }

}
