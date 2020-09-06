package io.craigmiller160.authmanagementservice.integration.graphql

import com.fasterxml.jackson.databind.ObjectMapper
import com.graphql.spring.boot.test.GraphQLResponse
import com.graphql.spring.boot.test.GraphQLTestTemplate
import io.craigmiller160.authmanagementservice.integration.AbstractOAuthTest
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
abstract class AbstractGraphqlTest : AbstractOAuthTest() {

    @Autowired
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    protected lateinit var graphqlRestTemplate: GraphQLTestTemplate

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun graphqlSetup() {
        graphqlRestTemplate.addHeader("Authorization", "Bearer $token")
    }

    protected fun <T> parseResponse(response: GraphQLResponse, type: Class<T>): Response<T> {
        return objectMapper.readValue(
                response.rawResponse.body,
                objectMapper.typeFactory.constructParametricType(Response::class.java, type)
        )
    }

    class Response<T> (
            val data: T
    )

}