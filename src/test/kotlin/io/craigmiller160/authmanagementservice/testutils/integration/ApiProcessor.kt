package io.craigmiller160.authmanagementservice.testutils.integration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class ApiProcessor (
        private val mockMvc: MockMvc,
        private val objectMapper: ObjectMapper,
        private val isSecure: Boolean = false,
        private val authToken: String? = null
) {

    fun testGet(uri: String, uriVars: Array<Any> = arrayOf(), status: Int = 200): MvcResult {
        val reqBuilder = commonRequest(MockMvcRequestBuilders.get(uri, *uriVars))
        return mockMvc.perform(reqBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().`is`(status))
                .andReturn()
    }

    fun testPost(uri: String, body: Any, uriVars: Array<Any> = arrayOf(), status: Int = 200): MvcResult {
        val requestBuilder = commonBodyRequest(MockMvcRequestBuilders.post(uri, uriVars), body)
        return mockMvc.perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().`is`(status))
                .andReturn()
    }

    private fun commonBodyRequest(reqBuilder: MockHttpServletRequestBuilder, body: Any): MockHttpServletRequestBuilder {
        return commonRequest(reqBuilder)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(body))
    }

    private fun commonRequest(reqBuilder: MockHttpServletRequestBuilder): MockHttpServletRequestBuilder {
        var newReqBuilder = reqBuilder.secure(isSecure)
        newReqBuilder = authToken?.let {
            reqBuilder.header("Authorization", "Bearer $it")
        } ?: reqBuilder
        return newReqBuilder
    }

}
