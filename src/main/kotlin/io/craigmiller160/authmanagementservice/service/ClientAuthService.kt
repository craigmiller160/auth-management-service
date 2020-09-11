package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.ClientAuthDetailsDto
import io.craigmiller160.authmanagementservice.dto.UserAuthDetailsDto
import io.craigmiller160.authmanagementservice.exception.EntityNotFoundException
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.RefreshTokenRepository
import org.springframework.stereotype.Service

@Service
class ClientAuthService (
        private val clientRepo: ClientRepository,
        private val refreshTokenRepo: RefreshTokenRepository
) {

    fun getAuthDetailsForClient(clientId: Long): ClientAuthDetailsDto {
        val client = clientRepo.findById(clientId)
                .orElseThrow { EntityNotFoundException("No client for ID $clientId") }

        val tokens = refreshTokenRepo.findByClientIdAndUserIdIsNotNull(clientId)
        // TODO do need to lookup user email
        val authDetails = tokens.map { UserAuthDetailsDto(
                tokenId = it.id,
                clientId = clientId,
                clientName = client.name,
                userId = it.userId ?: 0,
                userEmail = null,
                lastAuthenticated = it.timestamp
        ) }
        println(authDetails) // TODO delete this
        return ClientAuthDetailsDto(
                clientName = client.name,
                userAuthDetails = authDetails
        )
    }

}
