package io.craigmiller160.authmanagementservice.testutils.integration

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class ApiProcessor (
        private val mockMvc: MockMvc,
        private val isSecure: Boolean = false,
        private val authToken: String? = null
) {

    fun testGet(uri: String, uriVars: Array<Any>, status: Int = 200): MvcResult {
        val reqBuilder = commonRequest(MockMvcRequestBuilders.get(uri, *uriVars))
        return mockMvc.perform(reqBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().`is`(status))
                .andReturn()
    }

    private fun commonRequest(reqBuilder: MockHttpServletRequestBuilder): MockHttpServletRequestBuilder {
        var newReqBuilder = reqBuilder.secure(isSecure)
        newReqBuilder = authToken?.let {
            reqBuilder.header("Authorization", "Bearer $it")
        } ?: reqBuilder
        return newReqBuilder
    }

}
