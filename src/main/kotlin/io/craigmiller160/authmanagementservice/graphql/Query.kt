package io.craigmiller160.authmanagementservice.graphql

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import org.springframework.stereotype.Component

@Component
class Query (
        private val clientRepo: ClientRepository
) : GraphQLQueryResolver {

    fun getAllClients(): List<Client> {
        return clientRepo.findAllByOrderByName()
    }

}
