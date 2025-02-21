package de.neuefische.backend.woa

import de.neuefische.backend.common.Medium
import org.bson.BsonObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface WorkOfArtRepo : MongoRepository<WorkOfArt, String> {
    fun findAllByMediumIn(mediums: List<Medium>): List<WorkOfArt>

    fun findAllByUser(user: BsonObjectId): List<WorkOfArt>
}
