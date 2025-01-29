package de.neuefische.backend.user

import de.neuefische.backend.common.Medium
import org.bson.BsonObjectId
import org.bson.codecs.pojo.annotations.BsonId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("user")
data class User(
    @BsonId
    val id: BsonObjectId = BsonObjectId(),
    @Indexed(unique = true)
    val name: String,
    val bio: String? = null,
    val imageUrl: String? = null,
    val mediums: List<Medium>? = emptyList(),
)
