package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.UserDto
import io.craigmiller160.authmanagementservice.olddto.FullUser
import io.craigmiller160.authmanagementservice.olddto.FullUserClient
import io.craigmiller160.authmanagementservice.olddto.UserList
import io.craigmiller160.authmanagementservice.entity.ClientUser
import io.craigmiller160.authmanagementservice.entity.ClientUserRole
import io.craigmiller160.authmanagementservice.entity.User
import io.craigmiller160.authmanagementservice.exception.EntityNotFoundException
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.ClientUserRepository
import io.craigmiller160.authmanagementservice.repository.ClientUserRoleRepository
import io.craigmiller160.authmanagementservice.repository.RoleRepository
import io.craigmiller160.authmanagementservice.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserService (
        private val userRepo: UserRepository,
        private val clientRepo: ClientRepository,
        private val roleRepo: RoleRepository,
        private val clientUserRoleRepo: ClientUserRoleRepository,
        private val clientUserRepo: ClientUserRepository
) {

    private val encoder = BCryptPasswordEncoder()

    fun getUsers(): UserList {
        val users = userRepo.findAllByOrderByEmail()
        return UserList(users)
    }

    fun getUsers2(): List<UserDto> {
        val users = userRepo.findAllByOrderByEmail()
        return users.map { UserDto.fromUser(it) }
    }

    @Transactional
    fun getUser(id: Long): FullUser? {
         return userRepo.findById(id).orElse(null)
                 ?.let { user ->
                     val clients = clientRepo.findAllByUserOrderByName(user.id)
                     val fullUserClients = clients.map { client ->
                         val roles = roleRepo.findAllByClientIdOrderByName(client.id)
                         val userRoles = roleRepo.findAllByClientAndUserOrderByName(client.id, user.id)
                         FullUserClient(client, userRoles, roles)
                     }
                     FullUser(user, fullUserClients)
                 }
    }

    fun createUser(user: User): FullUser {
        val newUser = userRepo.save(user)
        return FullUser(user, listOf())
    }

    @Transactional
    fun updateUser(id: Long, user: FullUser): FullUser {
        val existingUser = userRepo.findById(id)
                .orElseThrow { EntityNotFoundException("User not found for ID: $id") }
        val finalUser = user.user.copy(
                id = id,
                password = if (user.user.password.isNotBlank()) {
                    "{bcrypt}${encoder.encode(user.user.password)}"
                } else {
                    existingUser.password
                }
        )
        userRepo.save(finalUser)

        clientUserRoleRepo.deleteAllByUserId(id)
        clientUserRepo.deleteAllByUserId(id)

        user.clients.forEach { client ->
            val clientUser = ClientUser(0, id, client.client.id)
            clientUserRepo.save(clientUser)

            client.userRoles.forEach { role ->
                val clientUserRole = ClientUserRole(0, id, client.client.id, role.id)
                clientUserRoleRepo.save(clientUserRole)
            }
        }

        return getUser(id)!!
    }

    @Transactional
    fun deleteUser(id: Long): FullUser {
        val existing = getUser(id)
                ?: throw EntityNotFoundException("User not found for ID: $id")
        clientUserRoleRepo.deleteAllByUserId(id)
        clientUserRepo.deleteAllByUserId(id)
        userRepo.deleteById(id)
        return existing
    }

}
