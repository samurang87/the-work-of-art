package de.neuefische.backend.woa

import de.neuefische.backend.common.Medium
import org.springframework.data.mongodb.repository.MongoRepository

interface WorkOfArtRepo : MongoRepository<WorkOfArt, String> {
    fun findAllByMediumIn(mediums: List<Medium>): List<WorkOfArt>
}
