package io.craigmiller160.authmanagementservice.dto

import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.Role

data class FullUserClient (
        val client: Client,
        val userRoles: List<Role>,
        val otherRoles: List<Role>
) : Sanitizer<FullUserClient> {

    override fun sanitize(): FullUserClient {
        val client = this.client.sanitize()
        return FullUserClient(client, userRoles, otherRoles)
    }

}
