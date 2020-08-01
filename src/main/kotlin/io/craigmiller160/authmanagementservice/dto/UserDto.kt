package io.craigmiller160.authmanagementservice.dto

import io.craigmiller160.authmanagementservice.entity.User

data class UserDto (
        val id: Long,
        val email: String,
        val firstName: String,
        val lastName: String
) {
    companion object {
        fun fromUser(user: User): UserDto {
            return UserDto(
                    id = user.id,
                    email = user.email,
                    firstName = user.firstName,
                    lastName = user.lastName
            )
        }
    }
}
