package io.craigmiller160.authmanagementservice.security

import io.craigmiller160.oauth2.config.OAuthConfig
import io.craigmiller160.oauth2.service.TokenRefreshService
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component

@Component
class JwtFilterConfigurer (
        private val oAuthConfig: OAuthConfig,
        private val tokenRefreshService: TokenRefreshService
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain,HttpSecurity>() {

    override fun configure(http: HttpSecurity?) {
        http?.addFilterBefore(JwtValidationFilter(oAuthConfig, tokenRefreshService), UsernamePasswordAuthenticationFilter::class.java)
    }

}
