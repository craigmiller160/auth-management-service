package io.craigmiller160.authmanagementservice.graphql

import graphql.kickstart.tools.GraphQLResolver
import io.craigmiller160.authmanagementservice.dto.ClientDto
import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.service.ClientRoleService

class ClientDtoResolver (
        private val clientRoleService: ClientRoleService
) : GraphQLResolver<ClientDto> {

    fun roles(client: ClientDto): List<RoleDto> {
        return clientRoleService.getRoles2(client.id)
    }

}
