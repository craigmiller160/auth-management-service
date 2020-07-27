package io.craigmiller160.authmanagementservice.graphql.dto

import graphql.kickstart.tools.GraphQLResolver
import io.craigmiller160.authmanagementservice.dto.ClientUserDto
import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.service.LegacyClientService
import org.springframework.stereotype.Component

@Component
class ClientUserDtoResolver (
        private val legacyClientService: LegacyClientService
) : GraphQLResolver<ClientUserDto> {

    fun roles(clientUser: ClientUserDto): List<RoleDto> {
        return legacyClientService.getRolesForClientUser2(clientUser.clientId, clientUser.id)
    }

}
