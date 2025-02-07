package de.neuefische.backend.user

import de.neuefische.backend.exceptions.NotFoundException
import de.neuefische.backend.security.SecurityService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val securityService: SecurityService,
) {
    @GetMapping("/")
    fun getUser(
        @RequestParam(required = false) id: String?,
        @RequestParam(required = false) name: String?,
    ): ResponseEntity<UserProfileResponse> {
        require(!(id == null && name == null)) { "Either id or name must be provided" }

        var userProfile: UserProfileResponse? = null
        if (id != null) {
            userProfile = userService.getUserById(id)
        } else if (name != null) {
            userProfile = userService.getUserByName(name)
        }

        return if (userProfile != null) {
            ResponseEntity.ok(userProfile)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: String,
        @RequestBody req: UserProfileUpdateRequest,
    ): ResponseEntity<UserProfileResponse> {
        val currentUser =
            securityService.getCurrentUsername() ?: return ResponseEntity
                .status(
                    HttpStatus.UNAUTHORIZED,
                ).build()
        try {
            val updatedUser = userService.updateUser(id, req, currentUser)
            return ResponseEntity.ok(
                UserProfileResponse(
                    id = updatedUser.id.value.toString(),
                    name = updatedUser.name,
                    bio = updatedUser.bio,
                    imageUrl = updatedUser.imageUrl,
                    mediums = updatedUser.mediums?.map { it.lowercase } ?: emptyList(),
                ),
            )
        } catch (e: NotFoundException) {
            return ResponseEntity.notFound().build()
        } catch (e: AccessDeniedException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    }
}
