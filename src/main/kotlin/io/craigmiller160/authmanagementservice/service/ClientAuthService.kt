package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.dto.ClientAuthDetailsDto
import io.craigmiller160.authmanagementservice.repository.ClientRepository
import io.craigmiller160.authmanagementservice.repository.RefreshTokenRepository
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class ClientAuthService (
        private val clientRepo: ClientRepository,
        private val refreshTokenRepo: RefreshTokenRepository
) {

    fun getClientAuthDetails(clientId: Long): ClientAuthDetailsDto {
        clientRepo.findById(clientId)
                .orElseThrow { EntityNotFoundException("No auth details found for client $clientId") }

        val refreshToken = refreshTokenRepo.findByClientIdAndUserIdIsNull(clientId)
        return ClientAuthDetailsDto(
                tokenId = refreshToken?.id,
                clientId = clientId,
                lastAuthenticated = refreshToken?.timestamp
        )
    }

    fun clearClientAuthDetails(clientId: Long): ClientAuthDetailsDto {
        clientRepo.findById(clientId)
                .orElseThrow { EntityNotFoundException("No auth details found for client $clientId") }
        refreshTokenRepo.deleteByClientIdAndUserIdIsNull(clientId)
        return ClientAuthDetailsDto(
                tokenId = null,
                clientId = clientId,
                lastAuthenticated = null
        )
    }

}
