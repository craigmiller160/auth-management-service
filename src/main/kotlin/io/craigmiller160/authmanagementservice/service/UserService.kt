package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.UserList
import io.craigmiller160.authmanagementservice.entity.User
import io.craigmiller160.authmanagementservice.exception.EntityNotFoundException
import io.craigmiller160.authmanagementservice.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService (
        private val userRepo: UserRepository
) {

    // TODO unit tests

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

    fun updateUser(id: Long, user: User): User {
        val existing = userRepo.findById(id)
                .orElseThrow { EntityNotFoundException("User not found for ID: $id") }

        val finalUser = user.copy(id = id)
        return userRepo.save(finalUser)
    }

    fun deleteUser(id: Long): User {
        val existing = userRepo.findById(id)
                .orElseThrow { EntityNotFoundException("User not found for ID: $id") }
        userRepo.deleteById(id)
        return existing
    }

}
