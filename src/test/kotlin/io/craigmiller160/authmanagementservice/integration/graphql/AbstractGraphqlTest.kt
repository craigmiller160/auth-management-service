package io.craigmiller160.authmanagementservice.integration.graphql

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

    @BeforeEach
    fun graphqlSetup() {
        graphqlRestTemplate.addHeader("Authorization", "Bearer $token")
    }

    class Response<T> (
            val data: T
    )

}