package io.craigmiller160.authmanagementservice.integration.graphql.mutation

import io.craigmiller160.authmanagementservice.integration.graphql.AbstractGraphqlTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientMutationIntegrationTest : AbstractGraphqlTest() {

    override fun getGraphqlBasePath(): String {
        return "graphql/mutation/client"
    }

    @Test
    fun `mutation - client - createClient`() {
        TODO("Finish this")
    }

    @Test
    fun `mutation - client - updateClient`() {
        TODO("Finish this")
    }

    @Test
    fun `mutation - client - updateClient with secret`() {
        TODO("Finish this")
    }

    @Test
    fun `mutation - client - deleteClient`() {
        // TODO test clearing all related relationships too
        TODO("Finish this")
    }

}