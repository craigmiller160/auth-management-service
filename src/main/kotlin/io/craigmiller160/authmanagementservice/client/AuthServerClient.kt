package io.craigmiller160.authmanagementservice.client

import io.craigmiller160.authmanagementservice.config.OAuthConfig
import io.craigmiller160.authmanagementservice.dto.TokenRequest
import io.craigmiller160.authmanagementservice.dto.TokenResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate
import java.util.Base64

class AuthServerClient (
        private val restTemplate: RestTemplate,
        private val oAuthConfig: OAuthConfig
) {

    fun authCodeLogin(code: String): TokenResponse {
        val clientKey = oAuthConfig.clientKey
        val clientSecret = oAuthConfig.clientSecret
        val redirectUri = oAuthConfig.authCodeRedirectUri
        val host = oAuthConfig.authServerHost
        val path = oAuthConfig.tokenPath

        // TODO clean this up
//        val encodedAuthHeader = Base64.getEncoder().encodeToString("$clientKey:$clientSecret".toByteArray())

        val headers = HttpHeaders()
        headers.setBasicAuth(clientKey, clientSecret)
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val url = "$host$path"
        val request = TokenRequest(
                grant_type = "authorization_code",
                client_id = clientKey,
                code = code,
                redirect_uri = redirectUri
        )
        val response = restTemplate.exchange(url, HttpMethod.POST, HttpEntity<TokenRequest>(request, headers), TokenResponse::class.java)
        return response.body!! // TODO probably need better error handling here
    }

}
