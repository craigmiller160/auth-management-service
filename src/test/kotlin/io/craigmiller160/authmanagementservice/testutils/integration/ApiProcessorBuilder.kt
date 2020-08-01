package io.craigmiller160.authmanagementservice.testutils.integration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.test.web.servlet.MockMvc

class ApiProcessorBuilder (
        private val mockMvc: MockMvc,
        private val objectMapper: ObjectMapper
) {

    fun build(https: Boolean = false, authToken: String? = null): ApiProcessor {
        return ApiProcessor(mockMvc, objectMapper, https, authToken)
    }

}
