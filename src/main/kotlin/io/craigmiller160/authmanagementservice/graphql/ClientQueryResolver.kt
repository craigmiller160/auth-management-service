package io.craigmiller160.authmanagementservice.graphql

import graphql.kickstart.tools.GraphQLQueryResolver
import io.craigmiller160.authmanagementservice.dto.ClientDto
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.service.ClientService
import org.springframework.stereotype.Component

@Component
class ClientQueryResolver (
        private val clientRepo: ClientRepository,
        private val clientService: ClientService
) : GraphQLQueryResolver {

    // TODO sanitizer does not work here

    fun getClients(): List<ClientDto> {
        return clientService.getClients2()
    }

}
