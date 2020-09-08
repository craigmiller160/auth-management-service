package io.craigmiller160.authmanagementservice.dto

import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.ClientRedirectUri

data class ClientInputDto (
        val name: String,
        val clientKey: String,
        val clientSecret: String,
        val enabled: Boolean,
        val accessTokenTimeoutSecs: Int,
        val refreshTokenTimeoutSecs: Int,
        val authCodeTimeoutSecs: Int,
        val redirectUris: List<String>
) {

    fun getClientRedirectUris(clientId: Long = 0): List<ClientRedirectUri> =
            redirectUris.map { ClientRedirectUri(0, clientId, it) }

    fun toClient(clientId: Long = 0): Client {
        return Client(
                id = clientId,
                name = this.name,
                clientKey = this.clientKey,
                clientSecret = this.clientSecret,
                enabled = this.enabled,
                accessTokenTimeoutSecs = this.accessTokenTimeoutSecs,
                refreshTokenTimeoutSecs = this.refreshTokenTimeoutSecs,
                authCodeTimeoutSecs = this.authCodeTimeoutSecs
        )
    }
}
