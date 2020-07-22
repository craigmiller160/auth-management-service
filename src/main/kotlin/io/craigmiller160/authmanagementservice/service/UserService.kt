package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.UserList
import io.craigmiller160.authmanagementservice.entity.User
import io.craigmiller160.authmanagementservice.exception.EntityNotFoundException
import io.craigmiller160.authmanagementservice.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserService (
        private val userRepo: UserRepository
) {

    private val encoder = BCryptPasswordEncoder()

    fun getUsers(): UserList {
        val users = userRepo.findAllByOrderByEmail()
        return UserList(users)
    }

    fun getUser(id: Long): User? {
        return userRepo.findById(id).orElse(null)
    }

    fun createUser(user: User): User {
        return userRepo.save(user)
    }

    @Transactional
    fun updateUser(id: Long, user: User): User {
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
        val existing = userRepo.findById(id)
                .orElseThrow { EntityNotFoundException("User not found for ID: $id") }
        userRepo.deleteById(id)
        return existing
    }

}
