package io.craigmiller160.authmanagementservice.repository

import io.craigmiller160.authmanagementservice.entity.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface RoleRepository : JpaRepository<Role,Long> {

    fun findAllByClientIdOrderByName(clientId: Long): List<Role>

    fun findByClientIdAndId(clientId: Long, id: Long): Role?

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    fun deleteByClientIdAndId(clientId: Long, id: Long): Int

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    fun deleteByClientId(clientId: Long): Int

    @Query("""
        SELECT r
        FROM Role r
        JOIN ClientUserRole cur ON r.id = cur.roleId
        WHERE cur.clientId = :clientId
        AND cur.userId = :userId
        ORDER BY r.name ASC
    """)
    fun findAllByClientAndUserOrderByName(@Param("clientId") clientId: Long, @Param("userId") userId: Long): List<Role>

}
