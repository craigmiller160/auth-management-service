package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.FullClient
import io.craigmiller160.authmanagementservice.dto.FullUser
import io.craigmiller160.authmanagementservice.dto.FullUserClient
import io.craigmiller160.authmanagementservice.dto.UserList
import io.craigmiller160.authmanagementservice.entity.User
import io.craigmiller160.authmanagementservice.exception.EntityNotFoundException
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.RoleRepository
import io.craigmiller160.authmanagementservice.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserService (
        private val userRepo: UserRepository,
        private val clientRepo: ClientRepository,
        private val roleRepo: RoleRepository
) {

    private val encoder = BCryptPasswordEncoder()

    fun getUsers(): UserList {
        val users = userRepo.findAllByOrderByEmail()
        return UserList(users)
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
    fun updateUser(id: Long, user: User): User {
        // TODO need to save the rest of the data here
        val existing = userRepo.findById(id)
                .orElseThrow { EntityNotFoundException("User not found for ID: $id") }

        val finalUser = user.copy(
                id = id,
                password = if (user.password.isNotBlank()) {
                    "{bcrypt}${encoder.encode(user.password)}"
                } else {
                    existing.password
                }
        )
        return userRepo.save(finalUser)
    }

    @Transactional
    fun deleteUser(id: Long): User {
        // TODO need to return the rest of the data here
        // TODO also need to delete the other references here
        val existing = userRepo.findById(id)
                .orElseThrow { EntityNotFoundException("User not found for ID: $id") }
        userRepo.deleteById(id)
        return existing
    }

}