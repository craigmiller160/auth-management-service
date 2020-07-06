package io.craigmiller160.authmanagementservice.dto

import io.craigmiller160.authmanagementservice.security.AuthenticatedUser

data class AuthUserDto (
        val username: String,
        val roles: List<String>,
        val firstName: String,
        val lastName: String
) {

    companion object {

        fun fromAuthenticatedUser(authUser: AuthenticatedUser): AuthUserDto {
            return AuthUserDto(
                    username = authUser.username,
                    firstName = authUser.firstName,
                    lastName = authUser.lastName,
                    roles = authUser.authorities.map { it.authority }
            )
        }

    }

}