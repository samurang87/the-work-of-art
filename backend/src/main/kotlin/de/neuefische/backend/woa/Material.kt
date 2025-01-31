package de.neuefische.backend.woa

import de.neuefische.backend.common.Medium

data class Material(
    val name: String,                  // Cadmium Yellow
    val identifier: String? = null,    // 24
    val brand: String? = null,         // Schmincke
    val line: String? = null,          // Horadam
    val type: String? = null,          // Tube
    val medium: Medium? = null         // Watercolor
)
