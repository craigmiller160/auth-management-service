package io.craigmiller160.authmanagementservice.integration.graphql.mutation

import io.craigmiller160.authmanagementservice.integration.graphql.AbstractGraphqlTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserMutationIntegrationTest : AbstractGraphqlTest() {

    override fun getGraphqlBasePath(): String {
        return "graphql/mutation/user"
    }

    @Test
    fun `mutation - user - craeteUser`() {
        TODO("Finish this")
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

}