package io.craigmiller160.authmanagementservice.config

import io.craigmiller160.authmanagementservice.security.AuthEntryPoint
import io.craigmiller160.authmanagementservice.security.JwtFilterConfigurer
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true
)
class WebSecurityConfig (
        private val jwtFilterConfigurer: JwtFilterConfigurer,
        private val authEntryPoint: AuthEntryPoint
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http?.let {
            http.csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .requiresChannel().anyRequest().requiresSecure()
                    .and()
                    .authorizeRequests()
                    .antMatchers("/authcode/**").permitAll()
                    .anyRequest().fullyAuthenticated()
                    .and()
                    .apply(jwtFilterConfigurer)
                    .and()
                    .exceptionHandling().authenticationEntryPoint(authEntryPoint)
        }
    }

}
