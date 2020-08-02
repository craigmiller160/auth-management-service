package io.craigmiller160.authmanagementservice.repository

import io.craigmiller160.authmanagementservice.entity.ClientUserRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface ClientUserRoleRepository : JpaRepository<ClientUserRole,Long> {

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    fun deleteAllByRoleIdAndClientId(roleId: Long, clientId: Long): Long

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    fun deleteAllByClientId(clientId: Long): Long

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    fun deleteAllByUserId(userId: Long): Long

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    fun deleteAllByRoleId(roleId: Long): Long

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    fun deleteAllByUserIdAndClientId(userId: Long, clientId: Long): Long

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    fun deleteByClientIdAndUserIdAndRoleId(clientId: Long, userId: Long, roleId: Long): Long

}
