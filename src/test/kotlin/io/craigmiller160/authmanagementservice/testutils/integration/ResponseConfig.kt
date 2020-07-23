package io.craigmiller160.authmanagementservice.testutils.integration

import com.fasterxml.jackson.databind.ObjectMapper

class ResponseConfig {
    var status: Int = 200
//    var convert: (mapper: ObjectMapper, content: String) -> T
    var responseType: Class<*>? = null
}
