package io.craigmiller160.authmanagementservice.testutils.integration

import com.fasterxml.jackson.databind.ObjectMapper

class ResponseConfig {
    var status: Int = 200
    // TODO make response type generic
    var convert: (mapper: ObjectMapper, content: String) -> Any = { _, content -> content }
}
