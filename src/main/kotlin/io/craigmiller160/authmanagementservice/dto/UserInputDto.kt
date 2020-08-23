package io.craigmiller160.authmanagementservice.dto

import io.craigmiller160.authmanagementservice.entity.User

data class UserInputDto (
        val email: String,
        val password: String,
        val firstName: String,
        val lastName: String,
        val enabled: Boolean
) {
    fun toUser(): User {
        return User(
                id = 0,
                email = this.email,
                password = this.password,
                firstName = this.firstName,
                lastName = this.lastName,
                enabled = this.enabled
        )
    }
}
