package io.craigmiller160.authmanagementservice.testutils.integration

data class Req (
        val path: String,
        val vars: List<Any> = listOf(),
        var body: Any? = null
)
