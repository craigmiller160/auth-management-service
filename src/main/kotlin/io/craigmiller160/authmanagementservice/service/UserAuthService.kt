package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.UserAuthDetailsDto
import io.craigmiller160.authmanagementservice.dto.UserAuthDetailsListDto
import io.craigmiller160.authmanagementservice.exception.EntityNotFoundException
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.RefreshTokenRepository
import io.craigmiller160.authmanagementservice.repository.UserRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserAuthService (
        private val refreshTokenRepo: RefreshTokenRepository,
        private val userRepo: UserRepository,
        private val clientRepo: ClientRepository
) {

    @Transactional
    fun getAllUserAuthDetails(userId: Long): UserAuthDetailsListDto {
        val user = userRepo.findById(userId)
                .orElseThrow { EntityNotFoundException("No user found for id $userId") }
        val authDetails = clientRepo.findAllByUserOrderByName(userId)
                .mapNotNull { client ->
                    val refreshToken = refreshTokenRepo.findByClientIdAndUserId(client.id, userId)
                    refreshToken?.let {
                        UserAuthDetailsDto(
                                tokenId = it.id,
                                clientId = client.id,
                                clientName = client.name,
                                userId = userId,
                                userEmail = user.email,
                                lastAuthenticated = it.timestamp
                        )
                    }
                }
        return UserAuthDetailsListDto(user.email, authDetails)
    }

    @Transactional
    fun revokeUserAuthAccess(clientId: Long, userId: Long) {
        val user = userRepo.findByClientAndUser(clientId, userId)
                ?: throw EntityNotFoundException("No auth details found for client $clientId and user $userId")
        val client = clientRepo.findById(clientId)
                .orElseThrow { EntityNotFoundException("No client found for id $clientId") }

        refreshTokenRepo.deleteByClientIdAndUserId(clientId, userId)
    }

}
