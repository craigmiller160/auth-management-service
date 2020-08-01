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

    fun createRole(roleInput: RoleInputDto): RoleDto {
        return roleService.createRole(roleInput)
    }

    fun updateRole(id: Long, roleInput: RoleInputDto): RoleDto {
        return roleService.updateRole(id, roleInput)
    }

    fun deleteRole(id: Long): RoleDto {
        return roleService.deleteRole(id)
    }

}
