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

    fun get(path: String, vars: Array<Any> = arrayOf(), status: Int = 200): MvcResult {
        return request(HttpMethod.GET, path, vars, null, status)
    }

    fun post(path: String, vars: Array<Any> = arrayOf(), body: Any? = null, status: Int = 200): MvcResult {
        return request(HttpMethod.POST, path, vars, body, status)
    }

    private fun request(method: HttpMethod, path: String, vars: Array<Any>, body: Any?, status: Int): MvcResult {
        val commonBuilder = commonRequest(MockMvcRequestBuilders.get(path, *vars))
        val reqBuilder = when (method) {
            HttpMethod.GET -> commonRequest(MockMvcRequestBuilders.get(path, *vars))
            HttpMethod.POST -> commonBodyRequest(MockMvcRequestBuilders.post(path, *vars), body)
            else -> throw RuntimeException("Invalid HTTP method: $method")
        }

        return mockMvc.perform(reqBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().`is`(status))
                .andReturn()
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
