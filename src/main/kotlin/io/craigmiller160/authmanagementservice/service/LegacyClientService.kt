package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.ClientDto
import io.craigmiller160.authmanagementservice.dto.ClientUserDto
import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.dto.UserDto
import io.craigmiller160.authmanagementservice.olddto.FullClient
import io.craigmiller160.authmanagementservice.olddto.FullClientList
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.exception.EntityNotFoundException
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.ClientUserRepository
import io.craigmiller160.authmanagementservice.repository.ClientUserRoleRepository
import io.craigmiller160.authmanagementservice.repository.RoleRepository
import io.craigmiller160.authmanagementservice.repository.UserRepository
import io.craigmiller160.modelmapper.EnhancedModelMapper
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID
import javax.transaction.Transactional

@Service
class LegacyClientService (
        private val clientRepo: ClientRepository,
        private val userRepo: UserRepository,
        private val roleRepo: RoleRepository,
        private val clientUserRoleRepo: ClientUserRoleRepository,
        private val clientUserRepo: ClientUserRepository
) {

    private val encoder = BCryptPasswordEncoder()

    fun generateGuid(): String {
        return UUID.randomUUID().toString()
    }

    fun getClients(full: Boolean): FullClientList {
        val clients = clientRepo.findAllByOrderByName()
                .map { client ->
                    if (full) {
                        val users = userRepo.findAllByClientIdOrderByEmail(client.id)
                        val roles = roleRepo.findAllByClientIdOrderByName(client.id)
                        FullClient(client, users, roles)
                    } else {
                        FullClient(client, listOf(), listOf())
                    }
                }
        return FullClientList(clients)
    }

    fun getClientUsers2(clientId: Long): List<ClientUserDto> {
        val users = userRepo.findAllByClientIdOrderByEmail(clientId)
        return users.map { ClientUserDto.fromUser(it, clientId) }
    }

    fun getRolesForClientUser2(clientId: Long, userId: Long): List<RoleDto> {
        val roles = roleRepo.findAllByClientAndUserOrderByName(clientId, userId)
        return roles.map { RoleDto.fromRole(it) }
    }

    fun getClients2(): List<ClientDto> {
        val clients = clientRepo.findAllByOrderByName()
        return clients.map { ClientDto.fromClient(it) }
    }

    @Transactional
    fun getClient(id: Long): FullClient? {
        val result = clientRepo.findById(id).orElse(null)
        return result?.let { client ->
            val users = userRepo.findAllByClientIdOrderByEmail(client.id)
            val roles = roleRepo.findAllByClientIdOrderByName(client.id)
            return FullClient(client, users, roles)
        }
    }

    fun getClient2(id: Long): ClientDto? {
        val client = clientRepo.findById(id).orElse(null)
        return client?.let { ClientDto.fromClient(it) }
    }

    fun createClient(client: Client): Client {
        return clientRepo.save(client)
    }

    @Transactional
    fun updateClient(id: Long, client: Client): Client {
        val existing = clientRepo.findById(id)
                .orElseThrow { EntityNotFoundException("Client not found for ID: $id") }
        val finalClient = client.copy(
                id = id,
                clientSecret = if (client.clientSecret.isBlank()) {
                    "{bcrypt}${encoder.encode(existing.clientSecret)}"
                } else {
                    client.clientSecret
                }
        )
        return clientRepo.save(finalClient)
    }

    @Transactional
    fun deleteClient(id: Long): Client {
        val existing = clientRepo.findById(id)
                .orElseThrow { EntityNotFoundException("Client not found for ID: $id") }
        clientUserRoleRepo.deleteAllByClientId(id)
        clientUserRepo.deleteAllByClientId(id)
        clientRepo.deleteById(id)
        return existing
    }
}
