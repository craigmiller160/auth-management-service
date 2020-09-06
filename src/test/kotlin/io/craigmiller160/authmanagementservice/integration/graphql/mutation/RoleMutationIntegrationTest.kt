package io.craigmiller160.authmanagementservice.integration.graphql.mutation

import io.craigmiller160.authmanagementservice.integration.graphql.AbstractGraphqlTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoleMutationIntegrationTest : AbstractGraphqlTest() {

    override fun getGraphqlBasePath(): String {
        return "graphql/mutation/role"
    }

    @Test
    fun `mutation - role - createRole`() {
        TODO("Finish this")
    }

    @Test
    fun `mutation - role - updateRole`() {
        TODO("Finish this")
    }

    @Test
    fun `mutation - role - deleteRole`() {
        TODO("Finish this")
    }

}