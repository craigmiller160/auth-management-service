package io.craigmiller160.authmanagementservice.graphql.mutation

import graphql.kickstart.tools.GraphQLMutationResolver
import io.craigmiller160.authmanagementservice.service.RelationshipService
import org.springframework.stereotype.Component

@Component
class RelationshipMutationResolver (
        private val relationshipService: RelationshipService
) : GraphQLMutationResolver {

    fun removeUserFromClient(userId: Long, clientId: Long): Boolean {
        return relationshipService.removeUserFromClient(userId, clientId)
    }

}
