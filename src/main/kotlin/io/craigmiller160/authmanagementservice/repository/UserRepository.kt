package io.craigmiller160.authmanagementservice.repository

import io.craigmiller160.authmanagementservice.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User,Long> {

    // TODO unit tests

    fun findAllByOrderByEmail(): List<User>

    @Query("SELECT u FROM User u WHERE u.id IN (SELECT cu.userId FROM ClientUser cu WHERE cu.clientId = ?1)")
    fun findAllByClientIdOrderByEmail(clientId: Long): List<User>

    @Query("""
        SELECT u
        FROM User u
        WHERE u.id = (
            SELECT cu.userId
            FROM ClientUser cu
            WHERE cu.userId = :userId
            AND cu.clientId = :clientId
        )
    """)
    fun findByClientAndUser(@Param("clientId") clientId: Long, @Param("userId") userId: Long): User?

}
