package io.craigmiller160.authmanagementservice.repository

import io.craigmiller160.authmanagementservice.entity.ClientUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface ClientUserRepository : JpaRepository<ClientUser,Long> {

    @Transactional
    @Modifying
    fun deleteAllByClientId(clientId: Long): Long

    @Transactional
    @Modifying
    fun deleteAllByUserId(userId: Long): Long

}
