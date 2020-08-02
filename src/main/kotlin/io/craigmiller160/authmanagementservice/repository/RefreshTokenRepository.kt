package io.craigmiller160.authmanagementservice.repository

import io.craigmiller160.authmanagementservice.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken,String> {

    fun findByClientIdAndUserId(clientId: Long, userId: Long): RefreshToken?

    fun findByClientIdAndUserIdIsNull(clientId: Long): RefreshToken?

}
