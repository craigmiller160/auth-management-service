package io.craigmiller160.authmanagementservice.testutils.integration

import org.springframework.http.HttpMethod

data class Req (
        val method: HttpMethod,
        val path: String,
        val vars: List<Any> = listOf(),
        var body: Any? = null
)
