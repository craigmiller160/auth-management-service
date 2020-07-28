package io.craigmiller160.authmanagementservice.graphql.mutation

import graphql.kickstart.tools.GraphQLMutationResolver
import io.craigmiller160.authmanagementservice.dto.ClientDto
import io.craigmiller160.authmanagementservice.dto.ClientInputDto
import io.craigmiller160.authmanagementservice.service.ClientService
import org.springframework.stereotype.Component

@Component
class ClientMutationResolver (
        private val clientService: ClientService
) : GraphQLMutationResolver {

    fun createClient(client: ClientInputDto): ClientDto {
        return clientService.createClient(client)
    }

    fun updateClient(clientId: Long, client: ClientInputDto): ClientDto {
        return clientService.updateClient(clientId, client)
    }

    fun deleteClient(clientId: Long): ClientDto {
        return clientService.deleteClient(clientId)
    }

}
