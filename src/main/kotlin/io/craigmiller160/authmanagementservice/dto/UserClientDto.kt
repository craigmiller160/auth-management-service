package io.craigmiller160.authmanagementservice.dto

import io.craigmiller160.authmanagementservice.entity.Client

data class UserClientDto (
        val id: Long,
        val name: String,
        val clientKey: String,
        val userId: Long,
        val allRoles: List<RoleDto>,
        val userRoles: List<RoleDto>
) {
    companion object {
        fun fromClient(client: Client, userId: Long): UserClientDto {
            return UserClientDto(
                    id = client.id,
                    name = client.name,
                    clientKey = client.clientKey,
                    userId = userId,
                    allRoles = listOf(),
                    userRoles = listOf()
            )
        }
    }
}
