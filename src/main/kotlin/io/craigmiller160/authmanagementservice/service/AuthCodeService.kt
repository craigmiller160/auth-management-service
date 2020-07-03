package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.client.AuthServerClient
import io.craigmiller160.authmanagementservice.config.OAuthConfig
import io.craigmiller160.authmanagementservice.entity.ManagementRefreshToken
import io.craigmiller160.authmanagementservice.repository.ManagementRefreshTokenRepository
import org.springframework.stereotype.Service

@Service
class AuthCodeService (
        private val oAuthConfig: OAuthConfig,
        private val authServerClient: AuthServerClient,
        private val manageRefreshTokenRepo: ManagementRefreshTokenRepository
) {

    fun getAuthCodeLoginUrl(): String {
        val host = oAuthConfig.authServerHost
        val loginPath = oAuthConfig.authCodeLoginPath
        val redirectUri = oAuthConfig.authCodeRedirectUri // TODO need to URL encode this
        val clientKey = oAuthConfig.clientKey

        return "$host$loginPath?response_type=code&client_id=$clientKey&redirect_uri=$redirectUri"
    }

    fun code(code: String): String {
        val tokens = authServerClient.authCodeLogin(code)
        val manageRefreshToken = ManagementRefreshToken(0, tokens.tokenId, tokens.refreshToken)
        manageRefreshTokenRepo.save(manageRefreshToken)
        return tokens.accessToken
    }

}
