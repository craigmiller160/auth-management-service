package io.craigmiller160.authmanagementservice.graphql.query

import graphql.kickstart.tools.GraphQLQueryResolver
import io.craigmiller160.authmanagementservice.dto.ClientDto
import io.craigmiller160.authmanagementservice.service.ClientService
import org.springframework.stereotype.Component

@Component
class ClientQueryResolver (
        private val clientService: ClientService
) : GraphQLQueryResolver {

    fun getClients(): List<ClientDto> {
        return clientService.getAllClients()
    }

    fun getClient(clientId: Long): ClientDto? {
        return clientService.getClient(clientId)
    }

}
