package io.craigmiller160.authmanagementservice.olddto

import io.craigmiller160.authmanagementservice.dto.Sanitizer
import io.craigmiller160.authmanagementservice.entity.Client
import io.craigmiller160.authmanagementservice.entity.Role

data class FullUserClient (
        val client: Client,
        val userRoles: List<Role>,
        val allRoles: List<Role>
) : Sanitizer<FullUserClient> {

    override fun sanitize(): FullUserClient {
        val client = this.client.sanitize()
        return FullUserClient(client, userRoles, allRoles)
    }

}
