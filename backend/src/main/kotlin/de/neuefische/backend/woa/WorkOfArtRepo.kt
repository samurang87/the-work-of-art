package de.neuefische.backend.woa

import org.springframework.data.mongodb.repository.MongoRepository

interface WorkOfArtRepo : MongoRepository<WorkOfArt, String> {
}
