package io.craigmiller160.authmanagementservice.controller

import io.craigmiller160.authmanagementservice.dto.ClientAuthDetailsDto
import io.craigmiller160.authmanagementservice.service.GuidService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
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

    @GetMapping("/auth/{clientId}")
    fun getClientAuthDetails(@PathVariable clientId: Long): ClientAuthDetailsDto {
        TODO("Finish this")
    }

    @PostMapping("/auth/{clientId}/clear")
    fun clearClientAuthDetails(@PathVariable clientId: Long): ClientAuthDetailsDto {
        TODO("Finish this")
    }

}
