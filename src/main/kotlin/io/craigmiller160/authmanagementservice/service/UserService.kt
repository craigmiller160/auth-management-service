package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.UserClientDto
import io.craigmiller160.authmanagementservice.dto.UserDto
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService (
        private val userRepo: UserRepository,
        private val clientRepo: ClientRepository
) {

    fun getAllUsers(): List<UserDto> {
        val users = userRepo.findAllByOrderByEmail()
        return users.map { UserDto.fromUser(it) }
    }

    fun getUser(userId: Long): UserDto? {
        val user = userRepo.findById(userId).orElse(null)
        return user?.let { UserDto.fromUser(it) }
    }

    fun getClientsForUser(userId: Long): List<UserClientDto> {
        val clients = clientRepo.findAllByUserOrderByName(userId)
        return clients.map { UserClientDto.fromClient(it, userId) }
    }

}
