package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.ClientAuthDetailsDto
import io.craigmiller160.authmanagementservice.exception.EntityNotFoundException
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.RefreshTokenRepository
import org.springframework.stereotype.Service

@Service
class ClientAuthService (
        private val clientRepo: ClientRepository,
        private val refreshTokenRepo: RefreshTokenRepository
) {

    fun getClientAuthDetails(clientId: Long): ClientAuthDetailsDto {
        val client = clientRepo.findById(clientId)
                .orElseThrow { EntityNotFoundException("No auth details found for client $clientId") }

        val refreshToken = refreshTokenRepo.findByClientIdAndUserIdIsNull(clientId)
        return ClientAuthDetailsDto(
                tokenId = refreshToken?.id,
                clientId = clientId,
                clientName = client.name,
                lastAuthenticated = refreshToken?.timestamp
        )
    }

    fun revokeClientAuthAccess(clientId: Long): ClientAuthDetailsDto {
        val client = clientRepo.findById(clientId)
                .orElseThrow { EntityNotFoundException("No auth details found for client $clientId") }
        refreshTokenRepo.deleteByClientIdAndUserIdIsNull(clientId)
        return ClientAuthDetailsDto(
                tokenId = null,
                clientId = clientId,
                clientName = client.name,
                lastAuthenticated = null
        )
    }

}
