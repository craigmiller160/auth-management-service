package io.craigmiller160.authmanagementservice.testutils.integration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc

@Component
class ApiProcessorBuilder (
        private val mockMvc: MockMvc,
        private val objectMapper: ObjectMapper
) {

    fun build(isSecure: Boolean = false, authToken: String? = null): ApiProcessor {
        return ApiProcessor(mockMvc, objectMapper, isSecure, authToken)
    }

}
