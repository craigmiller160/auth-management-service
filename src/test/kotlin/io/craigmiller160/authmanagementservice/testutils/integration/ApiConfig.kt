package io.craigmiller160.authmanagementservice.testutils.integration

class ApiConfig {
    var req = RequestConfig()

    fun request(init: RequestConfig.() -> Unit) {
        val request = RequestConfig()
        request.init()
        req = request
    }
}
