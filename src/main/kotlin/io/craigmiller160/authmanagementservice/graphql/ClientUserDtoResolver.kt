package io.craigmiller160.authmanagementservice.graphql

import graphql.kickstart.tools.GraphQLResolver
import io.craigmiller160.authmanagementservice.dto.ClientDto
import io.craigmiller160.authmanagementservice.dto.ClientUserDto
import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.service.ClientService
import org.springframework.stereotype.Component

@Component
class ClientUserDtoResolver (
        private val clientService: ClientService
) : GraphQLResolver<ClientUserDto> {

    fun roles(clientUser: ClientUserDto): List<RoleDto> {
        return clientService.getRolesForClientUser2(clientUser.clientId, clientUser.id)
    }

}
