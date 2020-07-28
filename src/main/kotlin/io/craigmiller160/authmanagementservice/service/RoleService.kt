package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.dto.RoleInputDto
import io.craigmiller160.authmanagementservice.repository.RoleRepository
import org.springframework.stereotype.Service

@Service
class RoleService (
        private val roleRepo: RoleRepository
) {

    fun createRole(roleInput: RoleInputDto): RoleDto {
        val dbRole = roleRepo.save(roleInput.toRole())
        return RoleDto.fromRole(dbRole)
    }

}
