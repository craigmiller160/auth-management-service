package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.ClientList
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import org.springframework.stereotype.Service

@Service
class ClientService (
        private val clientRepo: ClientRepository
) {

    fun getClients(): ClientList {
        val clients = clientRepo.findAll()
        return ClientList(clients)
    }

    fun getClient(id: Long) {
        TODO("Finish this")
    }

    fun saveClient() {
        TODO("Finish this")
    }

    fun deleteClient() {
        TODO("Finish this")
    }

}
