package io.craigmiller160.authmanagementservice.dto

import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.Role
import io.craigmiller160.authmanagementservice.entity.User

data class FullClient (
        val client: Client,
        val users: List<User>,
        val roles: List<Role>
) : Sanitizer<FullClient> {

    override fun sanitize(): FullClient {
        val client = this.client.sanitize()
        val users = this.users.map { it.sanitize() }
        return FullClient(client, users, this.roles)
    }

}
