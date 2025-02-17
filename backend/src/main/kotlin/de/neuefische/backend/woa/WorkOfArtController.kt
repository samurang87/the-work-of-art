package de.neuefische.backend.woa

import de.neuefische.backend.common.Medium
import de.neuefische.backend.common.toMedium
import de.neuefische.backend.exceptions.NotFoundException
import de.neuefische.backend.security.SecurityService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/woa")
class WorkOfArtController(
    private val workOfArtService: WorkOfArtService,
    private val securityService: SecurityService,
) {
    @GetMapping("/{id}")
    fun getWorkOfArt(
        @PathVariable id: String,
    ): ResponseEntity<WorkOfArtResponse> {
        val workOfArt = workOfArtService.getWorkOfArtById(id)
        return if (workOfArt != null) {
            ResponseEntity.ok(workOfArt)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping()
    fun getAllWorksOfArt(
        @RequestParam(required = false) mediums: List<String>?,
    ): ResponseEntity<List<WorkOfArtShortResponse>> {
        if (mediums.isNullOrEmpty()) {
            val worksOfArt = workOfArtService.getAllWorksOfArt()
            return ResponseEntity.ok(worksOfArt)
        }
        val foundMediums: List<Medium> = mediums.mapNotNull { it.toMedium() }
        val worksOfArt = workOfArtService.getAllWorksOfArt(foundMediums)
        return ResponseEntity.ok(worksOfArt)
    }

    @PostMapping()
    fun createWorkOfArt(
        @RequestBody request: WorkOfArtCreateOrUpdateRequest,
    ): ResponseEntity<WorkOfArtResponse> {
        val workOfArt = workOfArtService.createWorkOfArt(request)
        val location = URI.create("/api/woa/${workOfArt.id}")
        return ResponseEntity.created(location).build()
    }

    @PutMapping("/{id}")
    fun updateWorkOfArt(
        @PathVariable id: String,
        @RequestBody request: WorkOfArtCreateOrUpdateRequest,
    ): ResponseEntity<WorkOfArtResponse> {
        val currentUser = securityService.getCurrentUsername() ?: ""
        try {
            val updatedWorkOfArt =
                workOfArtService.updateWorkOfArt(id, request, currentUser)
            return ResponseEntity.ok(updatedWorkOfArt)
        } catch (e: NotFoundException) {
            return ResponseEntity.notFound().build()
        } catch (e: AccessDeniedException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteWorkOfArt(
        @PathVariable id: String,
    ): ResponseEntity<String> {
        val currentUser = securityService.getCurrentUsername() ?: ""
        try {
            val deletedWoAid = workOfArtService.deleteWorkOfArt(id, currentUser)
            return ResponseEntity.ok(deletedWoAid)
        } catch (e: NotFoundException) {
            return ResponseEntity.notFound().build()
        } catch (e: AccessDeniedException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    }
}
