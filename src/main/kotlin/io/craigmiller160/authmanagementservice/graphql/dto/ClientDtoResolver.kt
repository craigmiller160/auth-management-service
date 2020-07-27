package io.craigmiller160.authmanagementservice.graphql.dto

import graphql.kickstart.tools.GraphQLResolver
import io.craigmiller160.authmanagementservice.dto.ClientDto
import io.craigmiller160.authmanagementservice.dto.ClientUserDto
import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.service.LegacyClientRoleService
import io.craigmiller160.authmanagementservice.service.LegacyClientService
import org.springframework.stereotype.Component

@Component

class ClientDtoResolver (
        private val legacyClientRoleService: LegacyClientRoleService,
        private val legacyClientService: LegacyClientService
) : GraphQLResolver<ClientDto> {

    fun roles(client: ClientDto): List<RoleDto> {
        return legacyClientRoleService.getRoles2(client.id)
    }

    fun users(client: ClientDto): List<ClientUserDto> {
        return legacyClientService.getClientUsers2(client.id)
    }

}
