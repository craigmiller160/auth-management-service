package io.craigmiller160.authmanagementservice.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.nimbusds.jose.jwk.JWKSet
import io.craigmiller160.authmanagementservice.testutils.JwtUtils
import io.craigmiller160.authmanagementservice.testutils.integration.ApiProcessor
import io.craigmiller160.authmanagementservice.testutils.integration.ApiProcessorBuilder
import io.craigmiller160.oauth2.config.OAuthConfig
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import java.security.KeyPair

@AutoConfigureMockMvc
abstract class AbstractControllerIntegrationTest : AbstractOAuthTest() {

    lateinit var apiProcessor: ApiProcessor

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun apiProcessorSetup() {
        apiProcessor = ApiProcessorBuilder(mockMvc, objectMapper).build(
                https = true,
                authToken = token
        )
    }

}
