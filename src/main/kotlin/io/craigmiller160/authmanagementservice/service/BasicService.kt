package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.User
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class BasicService ( // TODO ultimately delete this
        private val userRepo: UserRepository,
        private val clientRepo: ClientRepository
) {

    fun getUsers(): List<User> {
        return userRepo.findAll()
    }

    fun getClients(): List<Client> {
        return clientRepo.findAll()
    }

}
