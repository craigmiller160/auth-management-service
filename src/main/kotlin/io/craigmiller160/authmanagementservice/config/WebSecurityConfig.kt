package io.craigmiller160.authmanagementservice.config

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

class WebSecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http?.let {
            http.csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .requiresChannel().anyRequest().requiresSecure()
        }
    }

}