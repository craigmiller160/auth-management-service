package io.craigmiller160.authmanagementservice.security

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component

@Component
class JwtFilterConfigurer : SecurityConfigurerAdapter<DefaultSecurityFilterChain,HttpSecurity>() {

    override fun configure(http: HttpSecurity?) {
        http?.addFilterBefore(JwtValidationFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

}