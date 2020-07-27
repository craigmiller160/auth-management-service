package io.craigmiller160.authmanagementservice.controller

import io.craigmiller160.authmanagementservice.olddto.RoleList
import io.craigmiller160.authmanagementservice.entity.Role
import io.craigmiller160.authmanagementservice.service.LegacyClientRoleService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/clients/{clientId}/roles")
class ClientRoleController (
        private val legacyClientRoleService: LegacyClientRoleService
) {

    @GetMapping
    fun getRoles(@PathVariable clientId: Long): ResponseEntity<RoleList> {
        val roles = legacyClientRoleService.getRoles(clientId)
        if (roles.roles.isEmpty()) {
            return ResponseEntity.noContent().build()
        }
        return ResponseEntity.ok(roles)
    }

    @PostMapping
    fun createRole(@PathVariable clientId: Long, @RequestBody role: Role): Role {
        return legacyClientRoleService.createRole(clientId, role)
    }

    @PutMapping("/{roleId}")
    fun updateRole(@PathVariable clientId: Long, @PathVariable roleId: Long, @RequestBody role: Role): Role {
        return legacyClientRoleService.updateRole(clientId, roleId, role)
    }

    @DeleteMapping("/{roleId}")
    fun deleteRole(@PathVariable clientId: Long, @PathVariable roleId: Long): Role {
        return legacyClientRoleService.deleteRole(clientId, roleId)
    }

}
