package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.client.AuthServerClient
import io.craigmiller160.authmanagementservice.config.OAuthConfig
import org.springframework.stereotype.Service

@Service
class AuthCodeService (
        private val oAuthConfig: OAuthConfig,
        private val authServerClient: AuthServerClient
) {

    fun getAuthCodeLoginUrl(): String {
        val host = oAuthConfig.authServerHost
        val loginPath = oAuthConfig.authCodeLoginPath
        val redirectUri = oAuthConfig.authCodeRedirectUri // TODO need to URL encode this
        val clientKey = oAuthConfig.clientKey

        return "$host$loginPath?response_type=code&client_id=$clientKey&redirect_uri=$redirectUri"
    }

    fun code(code: String) {
        val tokens = authServerClient.authCodeLogin(code)
    }

}
