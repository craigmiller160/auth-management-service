package io.craigmiller160.authmanagementservice.controller

import io.craigmiller160.authmanagementservice.dto.ClientAuthDetailsDto
import io.craigmiller160.authmanagementservice.dto.OldClientAuthDetailsDto
import io.craigmiller160.authmanagementservice.service.ClientAuthService
import io.craigmiller160.authmanagementservice.service.GuidService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/clients")
class ClientController (
        private val guidService: GuidService,
        private val clientAuthService: ClientAuthService
) {

    @GetMapping("/guid")
    fun generateGuid(): String {
        return guidService.generateGuid()
    }

    // TODO update tests for this
    @GetMapping("/auth/{clientId}")
    fun getAuthDetailsForClient(@PathVariable clientId: Long): ClientAuthDetailsDto {
        return clientAuthService.getAuthDetailsForClient(clientId)
    }

}
