package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.UserAuthDetailsDto
import io.craigmiller160.authmanagementservice.repository.RefreshTokenRepository
import org.springframework.stereotype.Service

@Service
class UserAuthService (
        private val refreshTokenRepo: RefreshTokenRepository
) {

    fun getUserAuthDetails(clientId: Long, userId: Long): UserAuthDetailsDto {
        TODO("Finish this")
    }

    fun clearUserAuth(clientId: Long, userId: Long): UserAuthDetailsDto {
        TODO("Finish this")
    }

}
