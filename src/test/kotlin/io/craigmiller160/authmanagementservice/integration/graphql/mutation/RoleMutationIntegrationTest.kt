package io.craigmiller160.authmanagementservice.integration.graphql.mutation

import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.Role
import io.craigmiller160.authmanagementservice.integration.graphql.AbstractGraphqlTest
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
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoleMutationIntegrationTest : AbstractGraphqlTest() {

    @Autowired
    private lateinit var roleRepo: RoleRepository

    @Autowired
    private lateinit var clientRepo: ClientRepository

    private lateinit var client1: Client
    private lateinit var role1: Role

    @BeforeEach
    fun setup() {
        client1 = clientRepo.save(TestData.createClient(1))
        role1 = roleRepo.save(TestData.createRole(1, client1.id))
    }

    @AfterEach
    fun clean() {
        roleRepo.deleteAll()
        clientRepo.deleteAll()
    }

    override fun getGraphqlBasePath(): String {
        return "graphql/mutation/role"
    }

    @Test
    fun `mutation - role - createRole`() {
        val result = execute("mutation_role_createRole", CreateRoleResponse::class.java)

        val expected = RoleDto(2, "New Role", client1.id)
        assertEquals(expected, result.createRole)

        assertEquals(2, roleRepo.count())
        val expectedDb = Role(2, "New Role", client1.id)
        assertEquals(expectedDb, roleRepo.findById(2).get())
    }

    @Test
    fun `mutation - role - updateRole`() {
        TODO("Finish this")
    }

    @Test
    fun `mutation - role - deleteRole`() {
        TODO("Finish this")
    }

    class CreateRoleResponse (
            val createRole: RoleDto
    )

    class UpdateRoleResponse (
            val updateRole: RoleDto
    )

    class DeleteRoleResponse (
            val deleteRole: RoleDto
    )

}