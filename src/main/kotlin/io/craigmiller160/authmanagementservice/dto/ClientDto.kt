package io.craigmiller160.authmanagementservice.dto

data class ClientDto (
        val id: Long,
        val name: String,
        val clientKey: String,
        val clientSecret: String,
        val enabled: Boolean,
        val allowClientCredentials: Boolean,
        val allowPassword: Boolean,
        val allowAuthCode: Boolean,
        val accessTokenTimeoutSecs: Int,
        val refreshTokenTimeoutSecs: Int,
        val roles: List<RoleDto> = listOf(),
        val users: List<UserDto> = listOf()
)
