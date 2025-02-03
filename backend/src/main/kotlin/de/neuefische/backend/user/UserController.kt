package de.neuefische.backend.user

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {

    @GetMapping("/")
    fun getUser(
        @RequestParam(required = false) id: String?,
        @RequestParam(required = false) name: String?
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
}
