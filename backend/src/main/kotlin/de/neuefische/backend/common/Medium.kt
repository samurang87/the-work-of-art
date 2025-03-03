package de.neuefische.backend.common

enum class Medium(
    val lowercase: String,
) {
    ACRYLIC("acrylic"),
    CHARCOAL("charcoal"),
    COLOR_PENCILS("color pencils"),
    CRAYONS("crayons"),
    DIGITAL("digital"),
    GOUACHE("gouache"),
    INK("ink"),
    MARKERS("markers"),
    OIL("oil"),
    PAN_PASTELS("pan pastels"),
    PASTELS("pastels"),
    PENCILS("pencils"),
    WATERCOLORS("watercolors"),
}

fun String.toMedium(): Medium? = Medium.entries.find { it.lowercase.equals(this, ignoreCase = true) }
