package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.dto.RoleInputDto
import io.craigmiller160.authmanagementservice.exception.EntityNotFoundException
import io.craigmiller160.authmanagementservice.repository.ClientUserRoleRepository
import io.craigmiller160.authmanagementservice.repository.RoleRepository
import org.springframework.stereotype.Service

@Service
class RoleService (
        private val roleRepo: RoleRepository,
        private val clientUserRoleRepo: ClientUserRoleRepository
) {

    fun createRole(roleInput: RoleInputDto): RoleDto {
        val dbRole = roleRepo.save(roleInput.toRole())
        return RoleDto.fromRole(dbRole)
    }

    fun updateRole(id: Long, roleInput: RoleInputDto): RoleDto {
        roleRepo.findById(id)
                .orElseThrow { EntityNotFoundException("No role to update for ID: $id") }

        val role = roleInput.toRole().copy(id = id)
        val dbRole = roleRepo.save(role)
        return RoleDto.fromRole(dbRole)
    }

    fun deleteRole(id: Long): RoleDto {
        val role = roleRepo.findById(id)
                .orElseThrow { EntityNotFoundException("No role to delete for ID: $id") }

        clientUserRoleRepo.deleteAllByRoleId(id)
        roleRepo.deleteById(id)
        return RoleDto.fromRole(role)
    }

}
