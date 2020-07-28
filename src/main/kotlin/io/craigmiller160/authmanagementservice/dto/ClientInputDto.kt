package io.craigmiller160.authmanagementservice.dto

import io.craigmiller160.authmanagementservice.entity.Client

data class ClientInputDto (
        val name: String,
        val clientKey: String,
        val clientSecret: String,
        val enabled: Boolean,
        val allowClientCredentials: Boolean,
        val allowPassword: Boolean,
        val allowAuthCode: Boolean,
        val accessTokenTimeoutSecs: Int,
        val refreshTokenTimeoutSecs: Int
) {
    fun toClient(): Client {
        return Client(
                id = 0,
                name = this.name,
                clientKey = this.clientKey,
                clientSecret = this.clientSecret,
                enabled = this.enabled,
                allowClientCredentials = this.allowClientCredentials,
                allowPassword = this.allowPassword,
                allowAuthCode = this.allowAuthCode,
                accessTokenTimeoutSecs = this.accessTokenTimeoutSecs,
                refreshTokenTimeoutSecs = this.refreshTokenTimeoutSecs
        )
    }
}
