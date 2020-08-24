package io.craigmiller160.authmanagementservice.dto

import io.craigmiller160.authmanagementservice.entity.Client

data class ClientDto (
        val id: Long,
        val name: String,
        val clientKey: String,
        val enabled: Boolean,
        val accessTokenTimeoutSecs: Int,
        val refreshTokenTimeoutSecs: Int,
        val roles: List<RoleDto>,
        val users: List<UserDto>
) {
    companion object {
        fun fromClient(client: Client): ClientDto {
            return ClientDto(
                    id = client.id,
                    name = client.name,
                    clientKey = client.clientKey,
                    enabled = client.enabled,
                    accessTokenTimeoutSecs = client.accessTokenTimeoutSecs,
                    refreshTokenTimeoutSecs = client.refreshTokenTimeoutSecs,
                    roles = listOf(),
                    users = listOf()
            )
        }
    }
}
