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

import io.craigmiller160.authmanagementservice.entity.ClientUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
interface ClientUserRepository : JpaRepository<ClientUser,Long> {

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    fun deleteAllByClientId(clientId: Long): Long

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    fun deleteAllByUserId(userId: Long): Long

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    fun deleteAllByUserIdAndClientId(userId: Long, clientId: Long): Long

}
