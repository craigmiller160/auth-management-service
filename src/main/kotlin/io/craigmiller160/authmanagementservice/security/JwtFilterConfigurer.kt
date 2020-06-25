package io.craigmiller160.authmanagementservice.security

import com.nimbusds.jose.jwk.JWKSet
import io.craigmiller160.authmanagementservice.config.AuthServerConfig
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import java.net.URL
import javax.annotation.PostConstruct

@Component
class JwtFilterConfigurer (
        private val authServerConfig: AuthServerConfig
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain,HttpSecurity>() {

    private lateinit var jwkSet: JWKSet

    @PostConstruct
    fun loadJWKSet() {
        jwkSet = JWKSet.load(URL("${authServerConfig.host}${authServerConfig.jwkPath}"))
    }

    override fun configure(http: HttpSecurity?) {
        http?.addFilterBefore(JwtValidationFilter(authServerConfig, jwkSet), UsernamePasswordAuthenticationFilter::class.java)
    }

}