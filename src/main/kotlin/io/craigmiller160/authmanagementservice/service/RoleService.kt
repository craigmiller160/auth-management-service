package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.entity.Role
import io.craigmiller160.authmanagementservice.repository.RoleRepository
import org.springframework.stereotype.Service

@Service
class RoleService (
        private val roleRepo: RoleRepository
) {

    fun createRole(name: String, clientId: Long): RoleDto {
        val role = Role(0, name, clientId)
        val dbRole = roleRepo.save(role)
        return RoleDto.fromRole(dbRole)
    }

}
