package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.UserAuthDetailsDto
import io.craigmiller160.authmanagementservice.repository.RefreshTokenRepository
import org.springframework.stereotype.Service

@Service
class UserAuthService (
        private val refreshTokenRepo: RefreshTokenRepository
) {

    fun getUserAuthDetails(clientId: Long, userId: Long): UserAuthDetailsDto {
        val refreshToken = refreshTokenRepo.findByClientIdAndUserId(clientId, userId)
        return UserAuthDetailsDto(
                tokenId = refreshToken?.refreshToken,
                clientId = clientId,
                userId = userId,
                lastAuthenticated = refreshToken?.timestamp
        )
    }

    fun clearUserAuth(clientId: Long, userId: Long): UserAuthDetailsDto {
        refreshTokenRepo.deleteByClientIdAndUserId(clientId, userId)
        return UserAuthDetailsDto(
                tokenId = null,
                clientId = clientId,
                userId = userId,
                lastAuthenticated = null
        )
    }

}
