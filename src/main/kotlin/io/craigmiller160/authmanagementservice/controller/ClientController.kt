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

import io.craigmiller160.authmanagementservice.dto.ClientAuthDetailsDto
import io.craigmiller160.authmanagementservice.service.ClientAuthService
import io.craigmiller160.authmanagementservice.service.GuidService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/clients")
class ClientController (
        private val guidService: GuidService,
        private val clientAuthService: ClientAuthService
) {

    @GetMapping("/guid")
    fun generateGuid(): String {
        return guidService.generateGuid()
    }

    @GetMapping("/auth/{clientId}")
    fun getAuthDetailsForClient(@PathVariable clientId: Long): ClientAuthDetailsDto {
        return clientAuthService.getAuthDetailsForClient(clientId)
    }

}
