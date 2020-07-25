package io.craigmiller160.authmanagementservice.repository

import io.craigmiller160.authmanagementservice.entity.ClientUserRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface ClientUserRoleRepository : JpaRepository<ClientUserRole,Long> {

    @Transactional
    @Modifying
    fun deleteAllByRoleIdAndClientId(roleId: Long, clientId: Long): Long

    @Transactional
    @Modifying
    fun deleteAllByClientId(clientId: Long): Long

    @Transactional
    @Modifying
    fun deleteAllByUserId(userId: Long): Long

}
