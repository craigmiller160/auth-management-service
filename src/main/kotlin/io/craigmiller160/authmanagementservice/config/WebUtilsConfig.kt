package io.craigmiller160.authmanagementservice.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = [
    "io.craigmiller160.webutils.controller"
])
class WebUtilsConfig
