package io.craigmiller160.authmanagementservice.testutils.integration

class ResponseConfig<T> {
    var status: Int = 200
    var responseType: Class<T>? = null
}
