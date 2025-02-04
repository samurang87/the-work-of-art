package de.neuefische.backend.woa

data class WorkOfArtShortResponse(
    val id: String,
    val user: String,
    val userName: String,
    val title: String,
    val imageUrl: String,
    val medium: String,
    val createdAt: String,
)
