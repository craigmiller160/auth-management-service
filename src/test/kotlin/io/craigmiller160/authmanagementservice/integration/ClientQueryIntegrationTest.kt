package io.craigmiller160.authmanagementservice.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.graphql.spring.boot.test.GraphQLTestTemplate
import graphql.kickstart.execution.GraphQLRequest
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.testutils.TestData
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.net.URI

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientQueryIntegrationTest : AbstractOAuthTest() {

    @Autowired
    private lateinit var graphqlRestTemplate: GraphQLTestTemplate

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

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
//
//    @Test
//    fun test2() {
//        val query = getGraphql("getAllClients")
//        graphqlRestTemplate.addHeader("Authorization", "Bearer $token")
//        val json = """{"query": "$query", "variables" null, "operationName": null}"""
//        val entity = RequestEntity.post(URI("/graphql"))
//                .header("Authorization", "Bearer $token")
//                .header("Content-Type", "application/json")
//                .body(json)
//
//        val result = restTemplate.exchange(entity, String::class.java)
//        println(result) // TODO delete this
//    }
//
//    @Test
//    fun test3() {
//        val query = getGraphql("getAllClients")
//        val request = GraphQLRequest(query, null, null)
//        val serializedRequest = objectMapper.writeValueAsString(request)
//        println(serializedRequest) // TODO delete this
//        val entity = RequestEntity.post(URI("/graphql"))
//                .header("Authorization", "Bearer $token")
//                .body(serializedRequest)
//
//        val result = restTemplate.exchange(entity, String::class.java)
//        println(result) // TODO delete this
//    }

}
