package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.ClientDto
import io.craigmiller160.authmanagementservice.dto.ClientUserDto
import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.RoleRepository
import io.craigmiller160.authmanagementservice.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class ClientService (
        private val roleRepo: RoleRepository,
        private val userRepo: UserRepository,
        private val clientRepo: ClientRepository
) {

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
        return clients.map { ClientDto.fromClient(it) }
    }

    fun getClient(clientId: Long): ClientDto? {
        val client = clientRepo.findById(clientId).orElse(null)
        return client?.let { ClientDto.fromClient(it) }
    }

}
