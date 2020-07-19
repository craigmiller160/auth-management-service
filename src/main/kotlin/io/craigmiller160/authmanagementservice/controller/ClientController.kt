package io.craigmiller160.authmanagementservice.controller

import io.craigmiller160.authmanagementservice.dto.ClientList
import io.craigmiller160.authmanagementservice.dto.FullClient
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.Role
import io.craigmiller160.authmanagementservice.service.ClientService
import org.apache.coyote.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/clients")
class ClientController (
        private val clientService: ClientService
) {

    // TODO unit tests

    @GetMapping("/guid")
    fun generateGuid(): String {
        return clientService.generateGuid()
    }

    @GetMapping
    fun getClients(): ResponseEntity<ClientList> {
        val clients = clientService.getClients()
        if (clients.clients.isEmpty()) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.ok(clients)
    }

    @GetMapping("/{id}")
    fun getClient(@PathVariable id: Long): ResponseEntity<FullClient> {
        return clientService.getClient(id)
                ?.let { ResponseEntity.ok(it) }
                ?: ResponseEntity.noContent().build()
    }

    @PostMapping
    fun createClient(@RequestBody client: Client): Client {
        return clientService.createClient(client)
    }

    @PutMapping("/{id}")
    fun updateClient(@PathVariable id: Long, @RequestBody client: Client): Client {
        return clientService.updateClient(id, client)
    }

    @DeleteMapping("/{id}")
    fun deleteClient(@PathVariable id: Long): Client {
        return clientService.deleteClient(id)
    }

    @PutMapping("/roles/{id}")
    fun updateRole(@PathVariable id: Long, @RequestBody role: Role): Role {
        return clientService.updateRole(id, role)
    }

    @PostMapping("/roles")
    fun createRole(@RequestBody role: Role): Role {
        return clientService.createRole(role)
    }

}
