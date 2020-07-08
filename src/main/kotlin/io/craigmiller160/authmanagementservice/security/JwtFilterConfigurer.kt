package io.craigmiller160.authmanagementservice.security

import io.craigmiller160.authmanagementservice.repository.ManagementRefreshTokenRepository
import io.craigmiller160.webutils.oauth2.AuthServerClient
import io.craigmiller160.webutils.oauth2.OAuthConfig
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
