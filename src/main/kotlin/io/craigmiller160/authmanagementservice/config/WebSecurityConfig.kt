package io.craigmiller160.authmanagementservice.config

import io.craigmiller160.oauth2.security.JwtValidationFilterConfigurer
import io.craigmiller160.webutils.security.AuthEntryPoint
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true
)
class WebSecurityConfig (
        private val jwtFilterConfigurer: JwtValidationFilterConfigurer,
        private val authEntryPoint: AuthEntryPoint,
        @Value("\${server.ssl.use-ssl}") private val useSsl: Boolean
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http?.let {
            it.csrf().disable()
                    .authorizeRequests()
                    .antMatchers(*jwtFilterConfigurer.defaultInsecurePathPatterns).permitAll()
                    .anyRequest().fullyAuthenticated()
                    .and()
                    .apply(jwtFilterConfigurer)
                    .and()
                    .exceptionHandling().authenticationEntryPoint(authEntryPoint)
            if (useSsl) {
                it.requiresChannel().anyRequest().requiresSecure()
            }
        }
    }

}
