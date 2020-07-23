package io.craigmiller160.authmanagementservice.testutils.integration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.lang.RuntimeException

class ApiProcessor (
        private val mockMvc: MockMvc,
        private val objectMapper: ObjectMapper,
        private val isSecure: Boolean = false,
        private val authToken: String? = null
) {

    fun call(init: ApiConfig.() -> Unit): ApiResult {
        val apiConfig = ApiConfig()
        apiConfig.init()

        val reqBuilder = when(apiConfig.req.method) {
            HttpMethod.GET -> commonRequest(MockMvcRequestBuilders.get(apiConfig.req.path, *apiConfig.req.vars))
            HttpMethod.POST -> commonBodyRequest(MockMvcRequestBuilders.post(apiConfig.req.path, *apiConfig.req.vars), apiConfig.req.body)
            else -> throw RuntimeException("Invalid HTTP method: ${apiConfig.req.method}")
        }

        val result = mockMvc.perform(reqBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().`is`(apiConfig.res.status))
                .andReturn()
        return ApiResult(result, objectMapper)
    }

    private fun commonBodyRequest(reqBuilder: MockHttpServletRequestBuilder, body: Any?): MockHttpServletRequestBuilder {
        val commonBuilder = commonRequest(reqBuilder)
        return body?.let {
            commonBuilder.contentType("application/json")
                .content(objectMapper.writeValueAsString(body))
        } ?: commonBuilder
    }

    private fun commonRequest(reqBuilder: MockHttpServletRequestBuilder): MockHttpServletRequestBuilder {
        var newReqBuilder = reqBuilder.secure(isSecure)
        newReqBuilder = authToken?.let {
            reqBuilder.header("Authorization", "Bearer $it")
        } ?: reqBuilder
        return newReqBuilder
    }

}
