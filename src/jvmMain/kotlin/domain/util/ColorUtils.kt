package domain.util

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
}