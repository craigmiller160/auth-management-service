package io.craigmiller160.authmanagementservice.dto

import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.ClientRedirectUri

data class ClientDto (
        val id: Long,
        val name: String,
        val clientKey: String,
        val enabled: Boolean,
        val accessTokenTimeoutSecs: Int,
        val refreshTokenTimeoutSecs: Int,
        val authCodeTimeoutSecs: Int,
        val roles: List<RoleDto> = listOf(),
        val users: List<ClientUserDto> = listOf(),
        val redirectUris: List<String> = listOf()
) {
    companion object {
        fun fromClient(client: Client, redirectUris: List<ClientRedirectUri> = listOf()): ClientDto {
            return ClientDto(
                    id = client.id,
                    name = client.name,
                    clientKey = client.clientKey,
                    enabled = client.enabled,
                    accessTokenTimeoutSecs = client.accessTokenTimeoutSecs,
                    refreshTokenTimeoutSecs = client.refreshTokenTimeoutSecs,
                    authCodeTimeoutSecs = client.authCodeTimeoutSecs,
                    redirectUris = redirectUris.map { it.redirectUri },
                    roles = listOf(),
                    users = listOf()
            )
        }
    }

    fun getClientRedirectUris(clientId: Long = 0): List<ClientRedirectUri> {
        return redirectUris.map { ClientRedirectUri(0, clientId, it) }
    }
}
