package io.craigmiller160.authmanagementservice.graphql

import graphql.kickstart.tools.GraphQLQueryResolver
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import org.springframework.stereotype.Component

@Component
class Query (
        private val clientRepo: ClientRepository
) : GraphQLQueryResolver {

    // TODO sanitizer does not work here

    fun getClients(): List<Client> {
        return clientRepo.findAllByOrderByName()
    }

}
