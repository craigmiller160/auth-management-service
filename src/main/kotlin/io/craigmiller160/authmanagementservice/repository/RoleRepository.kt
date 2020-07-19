package io.craigmiller160.authmanagementservice.repository

import io.craigmiller160.authmanagementservice.entity.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role,Long> {

    fun findAllByClientId(clientId: Long): List<Role>

}
