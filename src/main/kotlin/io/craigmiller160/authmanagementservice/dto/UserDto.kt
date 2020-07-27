package io.craigmiller160.authmanagementservice.dto

data class UserDto (
        val id: Long,
        val email: String,
        val firstName: String,
        val lastName: String,
        val password: String,
        val clients: List<ClientDto> = listOf()
)
