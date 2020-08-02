package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.UserAuthDetailsDto
import org.springframework.stereotype.Service

@Service
class UserAuthService {

    fun getUserAuthDetails(clientId: Long, userId: Long): UserAuthDetailsDto {
        TODO("Finish this")
    }

    fun clearUserAuth(clientId: Long, userId: Long): UserAuthDetailsDto {
        TODO("Finish this")
    }

}
