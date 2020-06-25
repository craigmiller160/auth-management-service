package io.craigmiller160.authmanagementservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "authmanageservice.authserver")
data class AuthServerConfig (
        var host: String = "",
        var jwkPath: String = ""
)