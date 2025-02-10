package de.neuefische.backend.common

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class MediumTest {
    @Test
    fun `toMedium should return correct Medium enum for valid lowercase string`() {
        assertEquals(Medium.ACRYLIC, "acrylic".toMedium())
        assertEquals(Medium.OIL, "oil".toMedium())
    }

    @Test
    fun `toMedium should return correct Medium enum for valid mixed case string`() {
        assertEquals(Medium.WATERCOLORS, "Watercolors".toMedium())
        assertEquals(Medium.INK, "InK".toMedium())
    }

    @Test
    fun `toMedium should return null for invalid string`() {
        assertNull("invalid".toMedium())
    }

    @Test
    fun `toMedium should return null for empty string`() {
        assertNull("".toMedium())
    }
}
