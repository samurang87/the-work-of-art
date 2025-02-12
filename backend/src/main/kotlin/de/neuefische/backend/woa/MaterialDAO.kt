package de.neuefische.backend.woa

data class MaterialDAO(
    val name: String,
    val identifier: String? = null,
    val brand: String? = null,
    val line: String? = null,
    val type: String? = null,
    val medium: String? = null,
)
