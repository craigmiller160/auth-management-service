package io.craigmiller160.authmanagementservice.repository

import io.craigmiller160.authmanagementservice.entity.ClientRedirectUri
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface ClientRedirectUriRepository : JpaRepository<ClientRedirectUri,Long> {

    fun findAllByClientId(clientId: Long): List<ClientRedirectUri>

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    fun deleteAllByClientId(clientId: Long): Int

}