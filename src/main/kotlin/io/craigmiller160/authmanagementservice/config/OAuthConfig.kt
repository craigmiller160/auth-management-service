package io.craigmiller160.authmanagementservice.config

import com.nimbusds.jose.jwk.JWKSet
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.lang.RuntimeException
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
        var cookieName: String = "",
        var postAuthRedirect: String = "",
        var cookieMaxAgeSecs: Long = 0
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    lateinit var jwkSet: JWKSet

    @PostConstruct
    fun loadJWKSet() {
        for (i in 0 until 5) {
            try {
                jwkSet = JWKSet.load(URL("$authServerHost$jwkPath"))
                return
            } catch (ex: Exception) {
                log.error("Error loading JWKSet", ex)
                Thread.sleep(1000)
            }
        }

        throw RuntimeException("Failed to load JWKSet")
    }

}
