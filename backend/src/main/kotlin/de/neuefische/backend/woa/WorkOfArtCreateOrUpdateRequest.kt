package de.neuefische.backend.woa

data class WorkOfArtCreateOrUpdateRequest(
    val user: String,
    val userName: String,
    val challengeId: String? = null,
    val title: String,
    val description: String? = null,
    val imageUrl: String,
    val medium: String,
    val materials: List<MaterialDAO> = emptyList(),
)
