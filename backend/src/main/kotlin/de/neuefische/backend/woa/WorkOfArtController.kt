package de.neuefische.backend.woa

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/woa")
class WorkOfArtController(private val workOfArtService: WorkOfArtService) {

    @GetMapping("/{id}")
    fun getWorkOfArt(@PathVariable id: String): ResponseEntity<WorkOfArtResponse> {

        val workOfArt = workOfArtService.getWorkOfArtById(id)
        return if (workOfArt != null) {
            ResponseEntity.ok(workOfArt)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
