package io.craigmiller160.authmanagementservice.integration.graphql.query

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
class RoleQueryIntegrationTest : AbstractGraphqlTest() {

    @Autowired
    private lateinit var clientRepo: ClientRepository

    @Autowired
    private lateinit var roleRepo: RoleRepository

    private lateinit var client1: Client
    private lateinit var role1: Role
    private lateinit var role2: Role
    private lateinit var role1Dto: RoleDto
    private lateinit var role2Dto: RoleDto

    @BeforeEach
    fun setup() {
        client1 = clientRepo.save(TestData.createClient(1))
        role1 = roleRepo.save(TestData.createRole(1, client1.id))
        role2 = roleRepo.save(TestData.createRole(2, client1.id))

        role1Dto = RoleDto.fromRole(role1)
        role2Dto = RoleDto.fromRole(role2)
    }

    @AfterEach
    fun clean() {
        roleRepo.deleteAll()
        clientRepo.deleteAll()
    }

    @Test
    fun `query - roles - for client`() {
        val response = graphqlRestTemplate.postForResource("graphql/query_roles_forClient.graphql")
        val result = parseResponse(response, RolesResponse::class.java)

        assertEquals(role1Dto, result.data.rolesForClient[0])
        assertEquals(role2Dto, result.data.rolesForClient[1])
    }

    class RolesResponse(
            val rolesForClient: List<RoleDto>
    )

}