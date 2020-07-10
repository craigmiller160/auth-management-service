package io.craigmiller160.authmanagementservice.controller

import io.craigmiller160.authmanagementservice.dto.ClientList
import io.craigmiller160.authmanagementservice.service.ClientService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/clients")
class ClientController (
        private val clientService: ClientService
) {

    @GetMapping
    fun getClients(): ClientList {
        return clientService.getClients()
    }

    @GetMapping("/{id}")
    fun getClient(@PathVariable id: Long) {
        TODO("Finish this")
    }

    @PostMapping
    fun saveClient() {
        TODO("Finish this")
    }

    @DeleteMapping("/{id}")
    fun deleteClient() {
        TODO("Finish this")
    }

}
