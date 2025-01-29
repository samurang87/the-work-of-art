package de.neuefische.backend.user

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("api/user")
class UserController(private val userService: UserService) {

    @GetMapping("/{name}")
    fun getUser(@PathVariable name: String): ResponseEntity<UserProfileResponse> {
        val userProfile = userService.getUserByName(name)
        return if (userProfile != null) {
            ResponseEntity.ok(userProfile)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
