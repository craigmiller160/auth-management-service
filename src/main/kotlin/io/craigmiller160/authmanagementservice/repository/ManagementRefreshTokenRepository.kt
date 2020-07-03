package io.craigmiller160.authmanagementservice.repository

import io.craigmiller160.authmanagementservice.entity.ManagementRefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ManagementRefreshTokenRepository : JpaRepository<ManagementRefreshToken,Long>