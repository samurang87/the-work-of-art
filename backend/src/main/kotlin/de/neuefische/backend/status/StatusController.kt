package de.neuefische.backend.status

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/status")
class StatusController {

    val githubSHA: String = System.getenv("GITHUB_SHA") ?: "ok"

    @GetMapping
    fun getStatus(): ResponseEntity<String> {
        return ResponseEntity.ok(githubSHA)
    }
}
