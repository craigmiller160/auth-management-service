package io.craigmiller160.authmanagementservice.dto

import io.craigmiller160.authmanagementservice.entity.Role

data class RoleDto (
        val id: Long,
        val name: String,
        val clientId: Long
) {
    companion object {
        fun fromRole(role: Role): RoleDto {
            return RoleDto(
                    id = role.id,
                    name = role.name,
                    clientId = role.clientId
            )
        }
    }
}
