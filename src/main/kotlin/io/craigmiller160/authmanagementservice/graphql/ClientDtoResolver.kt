package io.craigmiller160.authmanagementservice.graphql

import graphql.kickstart.tools.GraphQLResolver
import io.craigmiller160.authmanagementservice.dto.ClientDto
import io.craigmiller160.authmanagementservice.dto.ClientUserDto
import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.dto.UserDto
import io.craigmiller160.authmanagementservice.service.ClientRoleService
import io.craigmiller160.authmanagementservice.service.ClientService
import org.springframework.stereotype.Component

@Component

class ClientDtoResolver (
        private val clientRoleService: ClientRoleService,
        private val clientService: ClientService
) : GraphQLResolver<ClientDto> {

    fun roles(client: ClientDto): List<RoleDto> {
        return clientRoleService.getRoles2(client.id)
    }

    fun users(client: ClientDto): List<ClientUserDto> {
        return clientService.getClientUsers2(client.id)
    }

}
