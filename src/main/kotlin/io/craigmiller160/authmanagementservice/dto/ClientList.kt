package io.craigmiller160.authmanagementservice.dto

import io.craigmiller160.authmanagementservice.entity.Client

data class ClientList(val clients: List<Client>) : Sanitizer<ClientList> {
    override fun sanitize(): ClientList {
        return this.copy(clients = this.clients.map { it.sanitize() })
    }
}
