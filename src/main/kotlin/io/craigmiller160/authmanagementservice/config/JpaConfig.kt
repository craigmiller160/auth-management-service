package io.craigmiller160.authmanagementservice.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = [
    "io.craigmiller160.authmanagementservice.repository",
    "io.craigmiller160.oauth2.repository"
])
@EntityScan(basePackages = [
    "io.craigmiller160.authmanagementservice.entity",
    "io.craigmiller160.oauth2.entity"
])
class JpaConfig
