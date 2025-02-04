package de.neuefische.backend.user

data class UserProfileResponse(
    val id: String,
    val name: String,
    val bio: String?,
    val imageUrl: String?,
    val mediums: List<String>,
)
