package io.craigmiller160.authmanagementservice.graphql.mutation

import graphql.kickstart.tools.GraphQLMutationResolver
import io.craigmiller160.authmanagementservice.dto.ClientDto
import io.craigmiller160.authmanagementservice.dto.ClientInputDto
import org.springframework.stereotype.Component

@Component
class ClientMutationResolver : GraphQLMutationResolver {

    fun createClient(client: ClientInputDto): ClientDto {
        TODO("Finish this")
    }

    fun updateClient(clientId: Long, client: ClientInputDto): ClientDto {
        TODO("Finish this")
    }

    fun deleteClient(clientId: Long): ClientDto {
        TODO("Finish this")
    }

}
