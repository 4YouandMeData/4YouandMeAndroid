package org.fouryouandme.core.entity.theme

data class HEXColor(val hex: String)

data class HEXGradient(val primaryHex: String, val secondaryHex: String)

data class Theme(
    val primaryStart: HEXColor,
    val primaryEnd: HEXColor,
    val primaryGradient: HEXGradient
)