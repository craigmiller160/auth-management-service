package io.craigmiller160.authmanagementservice.testutils.integration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.test.web.servlet.MvcResult

class ApiResult (
        val mvcResult: MvcResult,
        private val objectMapper: ObjectMapper
) {

    val content = mvcResult.response.contentAsString
    val response = mvcResult.response

    fun <T> convert(action: (mapper: ObjectMapper, content: String) -> T): T {
        return action(objectMapper, content)
    }

}
