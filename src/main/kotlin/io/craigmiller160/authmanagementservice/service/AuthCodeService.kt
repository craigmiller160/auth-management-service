package io.craigmiller160.authmanagementservice.service

import io.craigmiller160.authmanagementservice.client.AuthServerClient
import io.craigmiller160.authmanagementservice.config.OAuthConfig
import io.craigmiller160.authmanagementservice.entity.ManagementRefreshToken
import io.craigmiller160.authmanagementservice.repository.ManagementRefreshTokenRepository
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Service
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Service
class AuthCodeService (
        private val oAuthConfig: OAuthConfig,
        private val authServerClient: AuthServerClient,
        private val manageRefreshTokenRepo: ManagementRefreshTokenRepository
) {

    fun getAuthCodeLoginUrl(state: String): String { // TODO add state to tests
        val host = oAuthConfig.authServerHost
        val loginPath = oAuthConfig.authCodeLoginPath
        val redirectUri = URLEncoder.encode(oAuthConfig.authCodeRedirectUri, StandardCharsets.UTF_8)
        val clientKey = URLEncoder.encode(oAuthConfig.clientKey, StandardCharsets.UTF_8)
        val encodedState = URLEncoder.encode(state, StandardCharsets.UTF_8)

        return "$host$loginPath?response_type=code&client_id=$clientKey&redirect_uri=$redirectUri&state=$encodedState"
    }

    fun code(code: String): Pair<ResponseCookie,String> {
        val tokens = authServerClient.authCodeLogin(code)
        val manageRefreshToken = ManagementRefreshToken(0, tokens.tokenId, tokens.refreshToken)
        manageRefreshTokenRepo.save(manageRefreshToken)
        val cookie = createCookie(tokens.accessToken, oAuthConfig.cookieMaxAgeSecs)
        return Pair(cookie, oAuthConfig.postAuthRedirect)
    }

    fun logout(): ResponseCookie {
        // TODO clear refresh token
        return createCookie("", 0)
    }

    private fun createCookie(token: String, maxAge: Long): ResponseCookie {
        return ResponseCookie
                .from(oAuthConfig.cookieName, token)
                .path("/")
                .secure(true)
                .httpOnly(true)
                .maxAge(oAuthConfig.cookieMaxAgeSecs)
                .sameSite("strict")
                .build()
    }

}
