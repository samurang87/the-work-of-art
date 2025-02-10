package de.neuefische.backend.user

data class UserProfileUpdateRequest(
    val bio: String?,
    val imageUrl: String?,
    val mediums: List<String>,
)
