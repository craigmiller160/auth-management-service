package io.craigmiller160.authmanagementservice.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = [
    "io.craigmiller160.webutils.controller",
    "io.craigmiller160.webutils.security",
    "io.craigmiller160.webutils.oauth2"
])
class WebUtilsConfig
