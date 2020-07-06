package io.craigmiller160.authmanagementservice.repository

import io.craigmiller160.authmanagementservice.entity.ManagementRefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface ManagementRefreshTokenRepository : JpaRepository<ManagementRefreshToken,Long> {

    fun findByTokenId(tokenId: String): ManagementRefreshToken? // TODO add to unit tests

    @Transactional
    @Modifying
    fun removeByTokenId(tokenId: String): Int // TODO add to unit tests

}
