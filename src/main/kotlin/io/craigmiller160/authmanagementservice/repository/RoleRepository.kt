package io.craigmiller160.authmanagementservice.repository

import io.craigmiller160.authmanagementservice.entity.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface RoleRepository : JpaRepository<Role,Long> {

    fun findAllByClientIdOrderByName(clientId: Long): List<Role>

    fun findByClientIdAndId(clientId: Long, id: Long): Role?

    @Transactional
    @Modifying
    fun deleteByClientIdAndId(clientId: Long, id: Long): Int

}
