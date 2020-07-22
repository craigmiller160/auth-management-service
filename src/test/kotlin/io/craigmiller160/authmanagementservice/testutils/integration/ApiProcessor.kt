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

    fun testGet(req: Req, status: Int = 200): MvcResult {
        val reqBuilder = commonRequest(MockMvcRequestBuilders.get(req.path, *req.vars.toTypedArray()))
        return mockMvc.perform(reqBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().`is`(status))
                .andReturn()
    }

    fun testPost(req: Req, status: Int = 200): MvcResult {
        val requestBuilder = commonBodyRequest(MockMvcRequestBuilders.post(req.path, *req.vars.toTypedArray()), req.body)
        return mockMvc.perform(requestBuilder)
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
