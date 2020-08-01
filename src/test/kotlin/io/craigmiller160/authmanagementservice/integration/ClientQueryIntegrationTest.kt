package io.craigmiller160.authmanagementservice.integration

import com.graphql.spring.boot.test.GraphQLTestTemplate
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.testutils.TestData
import org.junit.jupiter.api.AfterEach
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

    private lateinit var client1: Client
    private lateinit var client2: Client

    @BeforeEach
    fun setup() {
        client1 = clientRepo.save(TestData.createClient(1))
        client2 = clientRepo.save(TestData.createClient(2))
    }

    @AfterEach
    fun clean() {
        clientRepo.deleteAll()
    }

    protected fun getGraphql(name: String): String {
        val file = "graphql/$name.graphql"
        return javaClass.classLoader.getResourceAsStream(file)
                .use { it?.bufferedReader()?.readText() }
                ?: throw RuntimeException("Graphql file not found: $file")
    }

    @Test
    fun test() {
        val query = getGraphql("getAllClients") // TODO probably don't need this
        graphqlRestTemplate.addHeader("Authorization", "Bearer $token")
        val response = graphqlRestTemplate.postForResource("graphql/getAllClients.graphql")
        println(response.rawResponse.body) // TODO delete this
    }

}
