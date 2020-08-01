package io.craigmiller160.authmanagementservice.graphql.dto

import graphql.kickstart.tools.GraphQLResolver
import io.craigmiller160.authmanagementservice.dto.ClientUserDto
import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.service.ClientService
import org.springframework.stereotype.Component

@Component
class ClientUserDtoResolver (
        private val clientService: ClientService
) : GraphQLResolver<ClientUserDto> {

    fun roles(clientUser: ClientUserDto): List<RoleDto> {
        return clientService.getRolesForClientAndUser(clientUser.clientId, clientUser.id)
    }

}
