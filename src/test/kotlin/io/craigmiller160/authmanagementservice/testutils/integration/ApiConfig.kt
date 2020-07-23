package io.craigmiller160.authmanagementservice.testutils.integration

class ApiConfig<T> {
    var req = RequestConfig()
    var res = ResponseConfig<T>()

    fun request(init: RequestConfig.() -> Unit) {
        val request = RequestConfig()
        request.init()
        req = request
    }

    fun response(init: ResponseConfig<T>.() -> Unit) {
        val response = ResponseConfig<T>()
        response.init()
        res = response
    }
}
