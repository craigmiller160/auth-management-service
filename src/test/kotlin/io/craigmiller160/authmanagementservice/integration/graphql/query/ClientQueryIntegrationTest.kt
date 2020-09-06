package io.craigmiller160.authmanagementservice.integration.graphql.query

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.graphql.spring.boot.test.GraphQLTestTemplate
import io.craigmiller160.authmanagementservice.dto.ClientDto
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.integration.AbstractOAuthTest
import io.craigmiller160.authmanagementservice.repository.ClientRepository
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
class ClientQueryIntegrationTest : AbstractOAuthTest() {

    @Autowired
    private lateinit var graphqlRestTemplate: GraphQLTestTemplate

    @Autowired
    private lateinit var clientRepo: ClientRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private lateinit var client1: Client
    private lateinit var client2: Client

    @BeforeEach
    fun setup() {
        client1 = clientRepo.save(TestData.createClient(1))
        client2 = clientRepo.save(TestData.createClient(2))

        graphqlRestTemplate.addHeader("Authorization", "Bearer $token") // TODO adding it each time???
    }

    @AfterEach
    fun clean() {
        clientRepo.deleteAll()
    }

    @Test
    fun `query - clients - base client only`() {
        val response = graphqlRestTemplate.postForResource("graphql/query_clients_baseClientOnly.graphql")
        val body = response.rawResponse.body
        val result = objectMapper.readValue(body, object: TypeReference<Response<ClientsResponse>>(){})
        val client1Dto = ClientDto.fromClient(client1)
        val client2Dto = ClientDto.fromClient(client2)

        assertEquals(client1Dto, result.data.clients[0])
        assertEquals(client2Dto, result.data.clients[1])
    }

    @Test
    fun `query - single client - with roles and base users`() {
        TODO("Finish this")
    }

    @Test
    fun `query - single client - with roles and users with roles`() {
        TODO("Finish this")
    }

    @Test
    fun `query - single client - base client only`() {
        TODO("Finish this")
    }

    class ClientsResponse (
            val clients: List<ClientDto>
    )

    class Response<T> (
            val data: T
    )

}
