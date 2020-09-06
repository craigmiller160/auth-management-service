package io.craigmiller160.authmanagementservice.integration.graphql.mutation

import io.craigmiller160.authmanagementservice.dto.ClientDto
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.integration.graphql.AbstractGraphqlTest
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.testutils.TestData
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientMutationIntegrationTest : AbstractGraphqlTest() {

    @Autowired
    private lateinit var clientRepo: ClientRepository

    private val passwordEncoder = BCryptPasswordEncoder()

    private lateinit var client1: Client

    override fun getGraphqlBasePath(): String {
        return "graphql/mutation/client"
    }

    @BeforeEach
    fun setup() {
        client1 = clientRepo.save(TestData.createClient(1))
    }

    @AfterEach
    fun clean() {
        clientRepo.deleteAll()
    }

    private fun validateSecret(expected: String, actualHash: String) {
        val secretHash = actualHash.replace("{bcrypt}", "")
        assertTrue(passwordEncoder.matches(expected, secretHash))
    }

    @Test
    fun `mutation - client - createClient`() {
        val result = execute("mutation_client_createClient", CreateClientResponse::class.java)

        val expected = ClientDto(
                id = 2,
                name = "New Client",
                clientKey = "NewKey",
                enabled = true,
                accessTokenTimeoutSecs = 100,
                refreshTokenTimeoutSecs = 200
        )
        assertEquals(expected, result.createClient)

        val expectedDb = Client(
                id = 2,
                name = "New Client",
                clientKey = "NewKey",
                clientSecret = "",
                enabled = true,
                accessTokenTimeoutSecs = 100,
                refreshTokenTimeoutSecs = 200
        )
        val actualDb = clientRepo.findById(2).get()
        assertEquals(expectedDb, actualDb.copy(clientSecret = ""))
        validateSecret("NewSecret", actualDb.clientSecret)
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

    @Test
    fun `mutation - client - removeUserFromClient`() {
        // TODO test removal of roles too
        TODO("Finish this")
    }

    @Test
    fun `mutation - client - addUserToClient`() {
        TODO("Finish this")
    }

    class CreateClientResponse (
            val createClient: ClientDto
    )

    class UpdateClientResponse (
            val updateClient: ClientDto
    )

    class RemoveClientResponse (
            val removeClient: ClientDto
    )

}