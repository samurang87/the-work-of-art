package de.neuefische.backend.woa

import de.neuefische.backend.common.Medium
import org.bson.BsonObjectId
import org.bson.codecs.pojo.annotations.BsonId
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("woa")
data class WorkOfArt(
    @BsonId
    val id: BsonObjectId = BsonObjectId(),
    val user: BsonObjectId,
    val challengeId: BsonObjectId? = null,
    val userName: String,
    val title: String,
    val description: String? = null,
    val imageUrl: String,
    val medium: Medium,
    val materials: List<Material>? = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
