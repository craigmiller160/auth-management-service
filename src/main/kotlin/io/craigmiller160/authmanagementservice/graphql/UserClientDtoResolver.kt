package io.craigmiller160.authmanagementservice.graphql

import graphql.kickstart.tools.GraphQLResolver
import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.dto.UserClientDto
import io.craigmiller160.authmanagementservice.service.ClientRoleService
import org.springframework.stereotype.Component

@Component
class UserClientDtoResolver (
        private val clientRoleService: ClientRoleService
) : GraphQLResolver<UserClientDto> {

    fun allRoles(userClientDto: UserClientDto): List<RoleDto> {
        return clientRoleService.getRoles2(userClientDto.id)
    }

    fun userRoles(userClientDto: UserClientDto): List<RoleDto> {
        return clientRoleService.getUserRoles2(userClientDto.id, userClientDto.userId)
    }

}
