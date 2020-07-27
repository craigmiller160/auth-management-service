package io.craigmiller160.authmanagementservice.olddto

import io.craigmiller160.authmanagementservice.dto.Sanitizer

data class FullClientList(val clients: List<FullClient>): Sanitizer<FullClientList> {
    override fun sanitize(): FullClientList {
        val clients = this.clients.map { client ->
            val newClient = client.client.sanitize()
            val newUsers = client.users.map { user -> user.sanitize() }
            FullClient(newClient, newUsers, client.roles)
        }
        return FullClientList(clients)
    }
}
