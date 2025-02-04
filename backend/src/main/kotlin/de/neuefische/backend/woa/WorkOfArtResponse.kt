package de.neuefische.backend.woa

data class WorkOfArtResponse(
    val id: String,
    val user: String,
    val userName: String,
    val challengeId: String? = null,
    val title: String,
    val description: String? = null,
    val imageUrl: String,
    val medium: String,
    val materials: List<MaterialResponse>? = emptyList(),
    val createdAt: String,
)
