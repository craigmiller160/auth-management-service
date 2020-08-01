package io.craigmiller160.authmanagementservice.controller

import io.craigmiller160.authmanagementservice.service.GuidService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/clients")
class ClientController (
        private val guidService: GuidService
) {

    @GetMapping("/guid")
    fun generateGuid(): String {
        return guidService.generateGuid()
    }

}
