package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.RoleDto
import io.craigmiller160.authmanagementservice.olddto.RoleList
import io.craigmiller160.authmanagementservice.entity.Role
import io.craigmiller160.authmanagementservice.exception.EntityNotFoundException
import io.craigmiller160.authmanagementservice.repository.ClientUserRoleRepository
import io.craigmiller160.authmanagementservice.repository.RoleRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class ClientRoleService (
        private val roleRepo: RoleRepository,
        private val clientUserRoleRepo: ClientUserRoleRepository
) {

    fun createRole(clientId: Long, role: Role): Role {
        val finalRole = role.copy(id = 0, clientId = clientId)
        return roleRepo.save(finalRole)
    }

    @Transactional
    fun deleteRole(clientId: Long, roleId: Long): Role {
        val existing = roleRepo.findByClientIdAndId(clientId, roleId)
                ?: throw EntityNotFoundException("Role not found for ClientID $clientId and RoleID $roleId")
        clientUserRoleRepo.deleteAllByRoleIdAndClientId(roleId, clientId)
        roleRepo.deleteByClientIdAndId(clientId, roleId)
        return existing
    }

    fun getRoles(clientId: Long): RoleList {
        val roles = roleRepo.findAllByClientIdOrderByName(clientId)
        return RoleList(roles)
    }

    fun getRoles2(clientId: Long): List<RoleDto> {
        val roles = roleRepo.findAllByClientAndUserOrderByName(clientId)
        return roles.map { RoleDto.fromRole(role) }
    }

    @Transactional
    fun updateRole(clientId: Long, roleId: Long, role: Role): Role {
        roleRepo.findByClientIdAndId(clientId, roleId)
                ?: throw EntityNotFoundException("Role not found for ClientID $clientId and RoleID $roleId")
        val finalRole = role.copy(id = roleId, clientId = clientId)
        return roleRepo.save(finalRole)
    }

}
