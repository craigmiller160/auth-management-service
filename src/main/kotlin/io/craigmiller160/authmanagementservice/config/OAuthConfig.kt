package io.craigmiller160.authmanagementservice.config

import com.nimbusds.jose.jwk.JWKSet
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.net.URL
import javax.annotation.PostConstruct

@Configuration
@ConfigurationProperties(prefix = "authmanageservice.authserver")
data class OAuthConfig (
        var authServerHost: String = "",
        var jwkPath: String = "",
        var clientName: String = "",
        var clientKey: String = "",
        var clientSecret: String = "",
        var acceptBearerToken: Boolean = false,
        var acceptCookie: Boolean = false,
        var cookieName: String = ""
) {

    lateinit var jwkSet: JWKSet

    @PostConstruct
    fun loadJWKSet() {
        jwkSet = JWKSet.load(URL("$authServerHost$jwkPath"))
    }

}
