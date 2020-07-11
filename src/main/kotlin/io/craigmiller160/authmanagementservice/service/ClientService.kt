package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.ClientList
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.exception.EntityNotFoundException
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ClientService (
        private val clientRepo: ClientRepository
) {

    // TODO validate inputs

    fun generateGuid(): String {
        return UUID.randomUUID().toString()
    }

    fun getClients(): ClientList {
        val clients = clientRepo.findAll()
        return ClientList(clients)
    }

    fun getClient(id: Long): Client? {
        return clientRepo.findById(id).orElse(null)
    }

    fun createClient(client: Client): Client {
        return clientRepo.save(client)
    }

    fun updateClient(id: Long, client: Client): Client {
        val existing = clientRepo.findById(id)
                .orElseThrow { EntityNotFoundException("Client not found for ID: $id") }
        val finalClient = client.copy(
                id = id,
                clientSecret = if (client.clientSecret.isBlank()) existing.clientSecret else client.clientSecret
        )
        return clientRepo.save(client)
    }

    fun deleteClient(id: Long): Client {
        val existing = clientRepo.findById(id)
                .orElseThrow { EntityNotFoundException("Client not found for ID: $id") }
        clientRepo.deleteById(id)
        return existing
    }

}
