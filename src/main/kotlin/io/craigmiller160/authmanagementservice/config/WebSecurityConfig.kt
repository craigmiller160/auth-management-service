/*
 *     Auth Management Service
 *     Copyright (C) 2020 Craig Miller
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.craigmiller160.authmanagementservice.config

import io.craigmiller160.spring.oauth2.security.JwtValidationFilterConfigurer
import io.craigmiller160.webutils.security.AuthEntryPoint
import org.springframework.beans.factory.annotation.Value
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
        private val jwtFilterConfigurer: JwtValidationFilterConfigurer,
        private val authEntryPoint: AuthEntryPoint,
        @Value("\${server.ssl.use-ssl}") private val useSsl: Boolean
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http?.let {
            it.csrf().disable()
                    .authorizeRequests()
                    .antMatchers(*jwtFilterConfigurer.getInsecurePathPatterns()).permitAll()
                    .anyRequest().fullyAuthenticated()
                    .and()
                    .apply(jwtFilterConfigurer)
                    .and()
                    .exceptionHandling().authenticationEntryPoint(authEntryPoint)
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            if (useSsl) {
                it.requiresChannel().anyRequest().requiresSecure()
            }
        }
    }

}
