package io.craigmiller160.authmanagementservice.dto

import io.craigmiller160.authmanagementservice.entity.User

data class UserList(val users: List<User>) : Sanitizer<UserList> {
    override fun sanitize(): UserList {
        return this.copy(users = this.users.map { it.sanitize() })
    }
}
