package de.neuefische.backend.woa

import de.neuefische.backend.common.Medium

/**
 * Represents a material used in a work of art.
 *
 * @property name The name of the material (e.g., Cadmium Yellow).
 * @property identifier An optional identifier for the material (e.g., 24).
 * @property brand The brand of the material (e.g., Schmincke).
 * @property line The line of the material (e.g., Horadam).
 * @property type The type of the material (e.g., Tube).
 * @property medium The medium of the material (e.g., Watercolor).
 */
data class Material(
    val name: String,
    val identifier: String? = null,
    val brand: String? = null,
    val line: String? = null,
    val type: String? = null,
    val medium: Medium? = null,
)
