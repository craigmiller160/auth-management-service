package io.craigmiller160.authmanagementservice.integration.graphql

import com.fasterxml.jackson.databind.ObjectMapper
import com.graphql.spring.boot.test.GraphQLResponse
import com.graphql.spring.boot.test.GraphQLTestTemplate
import io.craigmiller160.authmanagementservice.integration.AbstractOAuthTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.annotation.DirtiesContext

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
abstract class AbstractGraphqlTest : AbstractOAuthTest() {

    @Autowired
    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    protected lateinit var graphqlRestTemplate: GraphQLTestTemplate

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val passwordEncoder = BCryptPasswordEncoder()

    @BeforeEach
    fun graphqlSetup() {
        graphqlRestTemplate.addHeader("Authorization", "Bearer $token")
    }

    protected abstract fun getGraphqlBasePath(): String

    private fun trailingSlash(value: String): String {
        if (value.endsWith("/")) {
            return value
        }

        return "$value/"
    }

    protected fun <T> execute(graphqlName: String, type: Class<T>): T {
        val graphqlFile = "${trailingSlash(getGraphqlBasePath())}$graphqlName.graphql"
        val response = graphqlRestTemplate.postForResource(graphqlFile)
        val result = parseResponse(response, type)
        return result.data
    }

    protected fun validateHash(rawValue: String, hash: String) {
        val secretHash = hash.replace("{bcrypt}", "")
        assertTrue(passwordEncoder.matches(rawValue, secretHash))
    }

    private fun <T> parseResponse(response: GraphQLResponse, type: Class<T>): Response<T> {
        return objectMapper.readValue(
                response.rawResponse.body,
                objectMapper.typeFactory.constructParametricType(Response::class.java, type)
        )
    }

    class Response<T> (
            val data: T
    )

}