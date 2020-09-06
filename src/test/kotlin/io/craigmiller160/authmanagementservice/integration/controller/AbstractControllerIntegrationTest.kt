package io.craigmiller160.authmanagementservice.integration.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.craigmiller160.apitestprocessor.ApiTestProcessor
import io.craigmiller160.apitestprocessor.config.AuthType
import io.craigmiller160.authmanagementservice.integration.AbstractOAuthTest
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc

@AutoConfigureMockMvc
abstract class AbstractControllerIntegrationTest : AbstractOAuthTest() {

    protected lateinit var apiProcessor: ApiTestProcessor

    @Autowired
    private lateinit var provMockMvc: MockMvc

    @Autowired
    private lateinit var provObjMapper: ObjectMapper

    @BeforeEach
    fun apiProcessorSetup() {
        apiProcessor = ApiTestProcessor {
            this.mockMvc = provMockMvc
            this.objectMapper = provObjMapper
            auth {
                type = AuthType.BEARER
                bearerToken = token
            }
        }
    }

}
