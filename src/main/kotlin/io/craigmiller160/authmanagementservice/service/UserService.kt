package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.UserClientDto
import io.craigmiller160.authmanagementservice.dto.UserDto
import io.craigmiller160.authmanagementservice.dto.UserInputDto
import io.craigmiller160.authmanagementservice.exception.EntityNotFoundException
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.ClientUserRepository
import io.craigmiller160.authmanagementservice.repository.ClientUserRoleRepository
import io.craigmiller160.authmanagementservice.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserService (
        private val userRepo: UserRepository,
        private val clientRepo: ClientRepository,
        private val clientUserRoleRepo: ClientUserRoleRepository,
        private val clientUserRepo: ClientUserRepository
) {

    private val encoder = BCryptPasswordEncoder()

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

    fun createUser(userInput: UserInputDto): UserDto {
        val encoded = encoder.encode(userInput.password)
        val user = userInput.toUser().copy(
                password = "{bcrypt}$encoded"
        )
        val dbUser = userRepo.save(user)
        return UserDto.fromUser(dbUser)
    }

    @Transactional
    fun updateUser(userId: Long, userInput: UserInputDto): UserDto {
        val existing = userRepo.findById(userId)
                .orElseThrow { EntityNotFoundException("No user exists to update for ID: $userId") }

        val user = userInput.toUser().copy(
                id = existing.id,
                password = if (userInput.password.isNotBlank()) {
                    val encoded = encoder.encode(userInput.password)
                    "{bcrypt}$encoded"
                } else {
                    existing.password
                }
        )
        val dbUser = userRepo.save(user)
        return UserDto.fromUser(dbUser)
    }

    @Transactional
    fun deleteUser(userId: Long): UserDto {
        val existing = userRepo.findById(userId)
                .orElseThrow { EntityNotFoundException("No user exists to delete for ID: $userId") }

        clientUserRoleRepo.deleteAllByUserId(userId)
        clientUserRepo.deleteAllByUserId(userId)
        userRepo.deleteById(userId)
        return UserDto.fromUser(existing)
    }

}
