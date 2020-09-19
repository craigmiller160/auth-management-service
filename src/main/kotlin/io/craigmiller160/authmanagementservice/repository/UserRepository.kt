/*
 *     Auth Management Service
 *     Copyright (C) 2020 Craig Miller
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.craigmiller160.authmanagementservice.repository

import io.craigmiller160.authmanagementservice.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User,Long> {

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
