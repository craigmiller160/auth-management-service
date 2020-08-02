package io.craigmiller160.authmanagementservice.repository

import io.craigmiller160.authmanagementservice.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken,String> {

    fun findByClientIdAndUserId(clientId: Long, userId: Long): RefreshToken?

    fun findByClientIdAndUserIdIsNull(clientId: Long): RefreshToken?

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    fun deleteByClientIdAndUserId(clientId: Long, userId: Long): Int

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    fun deleteByClientIdAndUserIdIsNull(clientId: Long): Int

}
