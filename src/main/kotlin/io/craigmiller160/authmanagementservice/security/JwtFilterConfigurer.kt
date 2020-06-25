package io.craigmiller160.authmanagementservice.security

import io.craigmiller160.authmanagementservice.config.AuthServerConfig
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component

@Component
class JwtFilterConfigurer (
        private val authServerConfig: AuthServerConfig
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain,HttpSecurity>() {

    override fun configure(http: HttpSecurity?) {
        http?.addFilterBefore(JwtValidationFilter(authServerConfig), UsernamePasswordAuthenticationFilter::class.java)
    }

}