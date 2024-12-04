package com.dr10.common.utilities

import androidx.compose.ui.graphics.Color

object ColorUtils {

    /**
     * Checks if a given [text] is a valid hexadecimal color code
     *
     * @param text The text to check for a valid hexadecimal color
     * @return True if the [text] is a valid hexadecimal color, false otherwise
     */
    fun isHexadecimalColor(text: String): Boolean{
        val pattern = Regex("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})\$")

        return pattern.matches(text)
    }

    /**
     * Converts a hexadecimal color string to a Compose [Color]
     *
     * @param hexString The hexadecimal color string to convert
     * @return The corresponding Compose [Color]
     */
    fun hexToColor(hexString: String): Color =
        Color(("ff" + hexString.removePrefix("#").lowercase()).toLong(16))


    /**
     * Convert a Compose [Color] to an AWT [java.awt.Color]
     *
     * @return A [java.awt.Color] instance representing the same color
     */
    fun Color.toAWTColor(): java.awt.Color =
        java.awt.Color(red, green, blue, alpha)

    /**
     * Converts a [String] to an AWT [java.awt.color]
     *
     * @param color The [String] to convert
     * @return [java.awt.color] The converted color
     */
    fun stringToColor(color: String): java.awt.Color {
        val colorValues = color.split(",").map { it.toInt() }
        return java.awt.Color(colorValues[0], colorValues[1], colorValues[2])
    }

    /**
     * Converts a [java.awt.Color] to a [String]
     *
     * @param color The [java.awt.Color] to convert
     * @return [String] The converted string
     */
    fun colorToString(color: java.awt.Color): String = "${color.red},${color.green},${color.blue}"

}