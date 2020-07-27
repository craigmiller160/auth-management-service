package io.craigmiller160.authmanagementservice.graphql.query

import graphql.kickstart.tools.GraphQLQueryResolver
import io.craigmiller160.authmanagementservice.dto.ClientDto
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.service.LegacyClientService
import org.springframework.stereotype.Component

@Component
class ClientQueryResolver (
        private val clientRepo: ClientRepository,
        private val legacyClientService: LegacyClientService
) : GraphQLQueryResolver {

    // TODO sanitizer does not work here

    fun getClients(): List<ClientDto> {
        return legacyClientService.getClients2()
    }

    fun getClient(id: Long): ClientDto? {
        return legacyClientService.getClient2(id)
    }

}
