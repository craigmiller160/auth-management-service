package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.ClientDto
import io.craigmiller160.authmanagementservice.dto.ClientInputDto
import io.craigmiller160.authmanagementservice.dto.ClientUserDto
import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.entity.ClientUser
import io.craigmiller160.authmanagementservice.exception.EntityNotFoundException
import io.craigmiller160.authmanagementservice.repository.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class ClientService (
        private val roleRepo: RoleRepository,
        private val userRepo: UserRepository,
        private val clientRepo: ClientRepository,
        private val clientUserRoleRepo: ClientUserRoleRepository,
        private val clientUserRepo: ClientUserRepository,
        private val clientRedirectUriRepo: ClientRedirectUriRepository
) {

    private val encoder = BCryptPasswordEncoder()

    fun getRolesForClient(clientId: Long): List<RoleDto> {
        val roles = roleRepo.findAllByClientIdOrderByName(clientId)
        return roles.map { RoleDto.fromRole(it) }
    }

    fun getUsersForClient(clientId: Long): List<ClientUserDto> {
        val users = userRepo.findAllByClientIdOrderByEmail(clientId)
        return users.map { ClientUserDto.fromUser(it, clientId) }
    }

    fun getRolesForClientAndUser(clientId: Long, userId: Long): List<RoleDto> {
        val roles = roleRepo.findAllByClientAndUserOrderByName(clientId, userId)
        return roles.map { RoleDto.fromRole(it) }
    }

    fun getAllClients(): List<ClientDto> {
        val clients = clientRepo.findAllByOrderByName()

        return clients.map {
            val redirectUris = clientRedirectUriRepo.findAllByClientId(it.id)
            ClientDto.fromClient(it, redirectUris)
        }
    }

    fun getClient(clientId: Long): ClientDto? {
        val client = clientRepo.findById(clientId).orElse(null)
        return client?.let {
            val redirectUris = clientRedirectUriRepo.findAllByClientId(it.id)
            ClientDto.fromClient(it, redirectUris)
        }
    }

    fun createClient(clientInput: ClientInputDto): ClientDto {
        val encoded = encoder.encode(clientInput.clientSecret)
        val client = clientInput.toClient().copy(
                clientSecret = "{bcrypt}$encoded"
        )
        val dbClient = clientRepo.save(client)
        val redirectUris = clientInput.getClientRedirectUris(dbClient.id)
        clientRedirectUriRepo.deleteAllByClientId(dbClient.id)
        val dbRedirectUris = clientRedirectUriRepo.saveAll(redirectUris)
        return ClientDto.fromClient(dbClient, dbRedirectUris)
    }

    @Transactional
    fun updateClient(clientId: Long, clientInput: ClientInputDto): ClientDto {
        val existing = clientRepo.findById(clientId)
                .orElseThrow { EntityNotFoundException("No client to update for ID: $clientId") }

        val client = clientInput.toClient().copy(
                id = clientId,
                clientSecret = if (clientInput.clientSecret.isNotBlank()) {
                    val encoded = encoder.encode(clientInput.clientSecret)
                    "{bcrypt}$encoded"
                } else {
                    existing.clientSecret
                }
        )
        val redirectUris = clientInput.getClientRedirectUris(clientId)
        val dbClient = clientRepo.save(client)
        clientRedirectUriRepo.deleteAllByClientId(clientId)
        val dbRedirectUris = clientRedirectUriRepo.saveAll(redirectUris)
        return ClientDto.fromClient(dbClient, dbRedirectUris)
    }

    @Transactional
    fun deleteClient(clientId: Long): ClientDto {
        val existing = clientRepo.findById(clientId)
                .orElseThrow { EntityNotFoundException("No client to delete for ID: $clientId") }
        val existingUris = clientRedirectUriRepo.findAllByClientId(clientId)

        clientUserRoleRepo.deleteAllByClientId(clientId)
        clientUserRepo.deleteAllByClientId(clientId)
        roleRepo.deleteByClientId(clientId)
        clientRedirectUriRepo.deleteAllByClientId(clientId)
        clientRepo.deleteById(clientId)
        return ClientDto.fromClient(existing, existingUris)
    }

    @Transactional
    fun removeUserFromClient(userId: Long, clientId: Long): List<ClientUserDto> {
        clientUserRoleRepo.deleteAllByUserIdAndClientId(userId, clientId)
        clientUserRepo.deleteAllByUserIdAndClientId(userId, clientId)
        return userRepo.findAllByClientIdOrderByEmail(clientId)
                .map { ClientUserDto.fromUser(it, clientId) }
    }

    @Transactional
    fun addClientToUser(userId: Long, clientId: Long): List<ClientUserDto> {
        val clientUser = ClientUser(0, userId, clientId)
        clientUserRepo.save(clientUser)
        return userRepo.findAllByClientIdOrderByEmail(clientId)
                .map { ClientUserDto.fromUser(it, clientId) }
    }

}
