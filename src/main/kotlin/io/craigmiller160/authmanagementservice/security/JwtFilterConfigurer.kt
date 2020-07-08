package io.craigmiller160.authmanagementservice.security

import io.craigmiller160.oauth2.client.AuthServerClient
import io.craigmiller160.oauth2.config.OAuthConfig
import io.craigmiller160.oauth2.repository.AppRefreshTokenRepository
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component

@Component
class JwtFilterConfigurer (
        private val oAuthConfig: OAuthConfig,
        private val appRefreshTokenRepo: AppRefreshTokenRepository,
        private val authServerClient: AuthServerClient
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain,HttpSecurity>() {

    override fun configure(http: HttpSecurity?) {
        http?.addFilterBefore(JwtValidationFilter(oAuthConfig, appRefreshTokenRepo, authServerClient), UsernamePasswordAuthenticationFilter::class.java)
    }

}
