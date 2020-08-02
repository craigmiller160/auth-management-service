package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.UserAuthDetailsDto
import io.craigmiller160.authmanagementservice.repository.RefreshTokenRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserAuthService (
        private val refreshTokenRepo: RefreshTokenRepository
) {

    @Transactional
    fun getUserAuthDetails(clientId: Long, userId: Long): UserAuthDetailsDto {
        // TODO validate that the user exists first
        val refreshToken = refreshTokenRepo.findByClientIdAndUserId(clientId, userId)
        return UserAuthDetailsDto(
                tokenId = refreshToken?.id,
                clientId = clientId,
                userId = userId,
                lastAuthenticated = refreshToken?.timestamp
        )
    }

    @Transactional
    fun clearUserAuth(clientId: Long, userId: Long): UserAuthDetailsDto {
        // TODO validate that the user exists first
        refreshTokenRepo.deleteByClientIdAndUserId(clientId, userId)
        return UserAuthDetailsDto(
                tokenId = null,
                clientId = clientId,
                userId = userId,
                lastAuthenticated = null
        )
    }

}
