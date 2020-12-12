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

import io.craigmiller160.authmanagementservice.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime
import javax.transaction.Transactional

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken,String> {

    fun findAllByUserId(userId: Long): List<RefreshToken>

    fun findByClientIdAndUserId(clientId: Long, userId: Long): RefreshToken?

    fun findByClientIdAndUserIdIsNotNull(clientId: Long): List<RefreshToken>

    // TODO update tests
    @Query("""
        SELECT r
        FROM RefreshToken r
        WHERE r.userId = :userId
        AND r.timestamp >= :oldestNotExpired
    """)
    fun findAllUserAuthentications(@Param("userId") userId: Long, @Param("oldestNotExpired") oldestNotExpired: ZonedDateTime): List<RefreshToken>

    // TODO update tests
    @Query("""
        SELECT r
        FROM RefreshToken r
        WHERE r.clientId = :clientId
        AND r.userId IS NOT NULL
        AND r.timestamp >= :oldestNotExpired
    """)
    fun findAllClientUserAuthentications(@Param("clientId") clientId: Long, @Param("oldestNotExpired") oldestNotExpired: ZonedDateTime): List<RefreshToken>

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    fun deleteByClientIdAndUserId(clientId: Long, userId: Long): Int

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    fun deleteAllByClientIdAndUserIdIsNull(clientId: Long): Int

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    fun deleteAllByClientId(clientId: Long): Int

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    fun deleteAllByUserId(userId: Long): Int

}
