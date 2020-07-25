package io.craigmiller160.authmanagementservice.repository

import io.craigmiller160.authmanagementservice.entity.Client
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository : JpaRepository<Client,Long> {

    fun findAllByOrderByName(): List<Client>

    @Query("""
        SELECT c 
        FROM Client c 
        JOIN ClientUser cu ON c.id = cu.clientId 
        WHERE cu.userId = :userId
        ORDER BY c.name ASC
    """)
    fun findAllByUserOrderByName(@Param("userId") userId: Long): List<Client>

}
