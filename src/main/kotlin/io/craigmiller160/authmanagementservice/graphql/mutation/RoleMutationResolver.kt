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

    fun createRole2(roleInput: RoleInputDto): RoleDto {
        return roleService.createRole(roleInput)
    }

}
