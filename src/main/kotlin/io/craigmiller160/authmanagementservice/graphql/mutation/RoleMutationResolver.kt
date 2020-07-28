package io.craigmiller160.authmanagementservice.graphql.mutation

import graphql.kickstart.tools.GraphQLMutationResolver
import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.dto.RoleInputDto
import io.craigmiller160.authmanagementservice.service.RoleService
import org.springframework.stereotype.Component

@Component
class RoleMutationResolver (
        private val roleService: RoleService
) : GraphQLMutationResolver {

    fun createRole(name: String, clientId: Long): RoleDto {
        return roleService.createRole(name, clientId)
    }

    fun createRole2(role: RoleInputDto): RoleDto {
        return roleService.createRole(role.name, role.clientId) // TODO refactor if this works
    }

}
