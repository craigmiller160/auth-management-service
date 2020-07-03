package io.craigmiller160.authmanagementservice.config

import com.nimbusds.jose.jwk.JWKSet
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.net.URL
import javax.annotation.PostConstruct

@Configuration
@ConfigurationProperties(prefix = "authmanageservice.oauth2")
data class OAuthConfig (
        var authServerHost: String = "",
        var jwkPath: String = "",
        var tokenPath: String = "",
        var authCodeLoginPath: String = "",
        var authCodeRedirectUri: String = "",
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
        // TODO add lazy loading or retry behavior
        jwkSet = JWKSet.load(URL("$authServerHost$jwkPath"))
    }

}
