package io.craigmiller160.authmanagementservice.graphql.query

import graphql.kickstart.tools.GraphQLQueryResolver
import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.service.ClientService
import org.springframework.stereotype.Component

@Component
class RoleQueryResolver (
        private val clientService: ClientService
) : GraphQLQueryResolver {

    fun rolesForClient(clientId: Long): List<RoleDto> {
        return clientService.getRolesForClient(clientId)
    }

}
