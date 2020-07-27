package io.craigmiller160.authmanagementservice.graphql

import graphql.kickstart.tools.GraphQLResolver
import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.dto.UserClientDto
import io.craigmiller160.authmanagementservice.service.LegacyClientRoleService
import org.springframework.stereotype.Component

@Component
class UserClientDtoResolver (
        private val legacyClientRoleService: LegacyClientRoleService
) : GraphQLResolver<UserClientDto> {

    fun allRoles(userClientDto: UserClientDto): List<RoleDto> {
        return legacyClientRoleService.getRoles2(userClientDto.id)
    }

    fun userRoles(userClientDto: UserClientDto): List<RoleDto> {
        return legacyClientRoleService.getUserRoles2(userClientDto.id, userClientDto.userId)
    }

}
