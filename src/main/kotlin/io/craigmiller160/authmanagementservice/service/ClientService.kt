package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.ClientList
import io.craigmiller160.authmanagementservice.dto.FullClient
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.Role
import io.craigmiller160.authmanagementservice.exception.EntityNotFoundException
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.RoleRepository
import io.craigmiller160.authmanagementservice.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID
import javax.transaction.Transactional

@Service
class ClientService (
        private val clientRepo: ClientRepository,
        private val userRepo: UserRepository,
        private val roleRepo: RoleRepository
) {

    // TODO validate inputs
    // TODO unit tests

    fun generateGuid(): String {
        return UUID.randomUUID().toString()
    }

    fun getClients(): ClientList {
        val clients = clientRepo.findAllByOrderByName()
        return ClientList(clients)
    }

    @Transactional
    fun getClient(id: Long): FullClient? {
        val result = clientRepo.findById(id).orElse(null)
        return result?.let { client ->
            val users = userRepo.findAllByClientId(client.id)
            val roles = roleRepo.findAllByClientId(client.id)
            return FullClient(client, users, roles)
        }
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
        return clientRepo.save(finalClient)
    }

    fun deleteClient(id: Long): Client {
        val existing = clientRepo.findById(id)
                .orElseThrow { EntityNotFoundException("Client not found for ID: $id") }
        clientRepo.deleteById(id)
        return existing
    }

    fun updateRole(id: Long, role: Role): Role {
        TODO("Finish this")
    }

    fun createRole(role: Role): Role {
        TODO("Finish this")
    }
}
