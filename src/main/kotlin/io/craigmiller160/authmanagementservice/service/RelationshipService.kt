package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.repository.ClientUserRepository
import io.craigmiller160.authmanagementservice.repository.ClientUserRoleRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class RelationshipService (
        private val clientUserRepository: ClientUserRepository,
        private val clientUserRoleRepository: ClientUserRoleRepository
) {

    @Transactional
    fun removeUserFromClient(userId: Long, clientId: Long): Boolean {
        clientUserRoleRepository.deleteAllByUserIdAndClientId(userId, clientId)
        clientUserRepository.deleteAllByUserIdAndClientId(userId, clientId)
        return true
    }

}
