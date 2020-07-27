package io.craigmiller160.authmanagementservice.dto

import io.craigmiller160.authmanagementservice.entity.User

data class ClientUserDto (
        val id: Long,
        val email: String,
        val firstName: String,
        val lastName: String,
        val password: String,
        val roles: List<RoleDto>
) {
    companion object {
        fun fromUser(user: User): ClientUserDto {
            return ClientUserDto(
                    id = user.id,
                    email = user.email,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    password = user.password,
                    roles = listOf()
            )
        }
    }
}
