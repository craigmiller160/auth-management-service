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

package io.craigmiller160.authmanagementservice.controller

import io.craigmiller160.authmanagementservice.dto.UserAuthDetailsListDto
import io.craigmiller160.authmanagementservice.service.UserAuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController (
        private val userAuthService: UserAuthService
) {

    @GetMapping("/auth/{userId}")
    fun getAllUserAuthDetails(@PathVariable userId: Long): UserAuthDetailsListDto {
        return userAuthService.getAllUserAuthDetails(userId)
    }

    @PostMapping("/auth/{userId}/{clientId}/revoke")
    fun revokeUserAuthAccess(@PathVariable clientId: Long, @PathVariable userId: Long): ResponseEntity<Void> {
        userAuthService.revokeUserAuthAccess(clientId, userId)
        return ResponseEntity.noContent().build()
    }

}
