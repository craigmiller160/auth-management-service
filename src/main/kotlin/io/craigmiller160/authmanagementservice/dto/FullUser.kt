package io.craigmiller160.authmanagementservice.dto

import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.Role
import io.craigmiller160.authmanagementservice.entity.User

data class FullUser (
        val user: User,
        val client: Client,
        val roles: List<Role>
) : Sanitizer<FullUser> {

    override fun sanitize(): FullUser {
        val user = this.user.sanitize()
        val client = this.client.sanitize()
        return FullUser(user, client, this.roles)
    }

}
