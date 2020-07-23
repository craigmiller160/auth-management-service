package io.craigmiller160.authmanagementservice.testutils.integration

import org.springframework.http.HttpMethod

class RequestConfig {
    var method: HttpMethod = HttpMethod.GET
    var path: String = ""
    var vars: Array<Any> = arrayOf()
    var body: Any? = null
    var doAuth: Boolean = true
}
