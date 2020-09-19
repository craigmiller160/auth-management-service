/*
 *     Auth Management Service
 *     Copyright (C) 2020 Craig Miller
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.dto.RoleInputDto
import io.craigmiller160.authmanagementservice.exception.EntityNotFoundException
import io.craigmiller160.authmanagementservice.repository.ClientUserRoleRepository
import io.craigmiller160.authmanagementservice.repository.RoleRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class RoleService (
        private val roleRepo: RoleRepository,
        private val clientUserRoleRepo: ClientUserRoleRepository
) {

    fun createRole(roleInput: RoleInputDto): RoleDto {
        val dbRole = roleRepo.save(roleInput.toRole())
        return RoleDto.fromRole(dbRole)
    }

    @Transactional
    fun updateRole(id: Long, roleInput: RoleInputDto): RoleDto {
        roleRepo.findById(id)
                .orElseThrow { EntityNotFoundException("No role to update for ID: $id") }

        val role = roleInput.toRole().copy(id = id)
        val dbRole = roleRepo.save(role)
        return RoleDto.fromRole(dbRole)
    }

    @Transactional
    fun deleteRole(id: Long): RoleDto {
        val role = roleRepo.findById(id)
                .orElseThrow { EntityNotFoundException("No role to delete for ID: $id") }

        clientUserRoleRepo.deleteAllByRoleId(id)
        roleRepo.deleteById(id)
        return RoleDto.fromRole(role)
    }

}
