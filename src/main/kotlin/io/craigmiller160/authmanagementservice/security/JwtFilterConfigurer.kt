package io.craigmiller160.authmanagementservice.security

import io.craigmiller160.authmanagementservice.client.AuthServerClient
import io.craigmiller160.authmanagementservice.config.OAuthConfig
import io.craigmiller160.authmanagementservice.repository.ManagementRefreshTokenRepository
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component

@Component
class JwtFilterConfigurer (
        private val oAuthConfig: OAuthConfig,
        private val manageRefreshTokenRepo: ManagementRefreshTokenRepository,
        private val authServerClient: AuthServerClient
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain,HttpSecurity>() {

    override fun configure(http: HttpSecurity?) {
        http?.addFilterBefore(JwtValidationFilter(oAuthConfig, manageRefreshTokenRepo, authServerClient), UsernamePasswordAuthenticationFilter::class.java)
    }

}
