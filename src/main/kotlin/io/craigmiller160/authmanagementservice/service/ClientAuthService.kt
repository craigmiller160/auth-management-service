package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.ClientAuthDetailsDto
import io.craigmiller160.authmanagementservice.dto.UserAuthDetailsDto
import io.craigmiller160.authmanagementservice.exception.EntityNotFoundException
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.RefreshTokenRepository
import io.craigmiller160.authmanagementservice.repository.UserRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class ClientAuthService (
        private val clientRepo: ClientRepository,
        private val userRepo: UserRepository,
        private val refreshTokenRepo: RefreshTokenRepository
) {

    @Transactional
    fun getAuthDetailsForClient(clientId: Long): ClientAuthDetailsDto {
        val client = clientRepo.findById(clientId)
                .orElseThrow { EntityNotFoundException("No client for ID $clientId") }

        val tokens = refreshTokenRepo.findByClientIdAndUserIdIsNotNull(clientId)
        val authDetails = tokens.map {
            val user = userRepo.findById(it.userId!!)
                    .orElseThrow { EntityNotFoundException("No user for ID: ${it.userId}") }
            UserAuthDetailsDto(
                    tokenId = it.id,
                    clientId = clientId,
                    clientName = client.name,
                    userId = it.userId ?: 0,
                    userEmail = user.email,
                    lastAuthenticated = it.timestamp
            )
        }
        return ClientAuthDetailsDto(
                clientName = client.name,
                userAuthDetails = authDetails
        )
    }

}
