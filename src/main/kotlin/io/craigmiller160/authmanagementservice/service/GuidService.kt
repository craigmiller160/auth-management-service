package io.craigmiller160.authmanagementservice.service

import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GuidService {

    fun generateGuid(): String {
        return UUID.randomUUID().toString()
    }

}
