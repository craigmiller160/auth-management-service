package io.craigmiller160.authmanagementservice.graphql.dto

import graphql.kickstart.tools.GraphQLResolver
import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.dto.UserClientDto
import io.craigmiller160.authmanagementservice.service.ClientService
import org.springframework.stereotype.Component

@Component
class UserClientDtoResolver (
        private val clientService: ClientService
) : GraphQLResolver<UserClientDto> {

    fun allRoles(userClientDto: UserClientDto): List<RoleDto> {
        return clientService.getRolesForClient(userClientDto.id)
    }

    fun userRoles(userClientDto: UserClientDto): List<RoleDto> {
        return clientService.getRolesForClientAndUser(userClientDto.id, userClientDto.userId)
    }

}
