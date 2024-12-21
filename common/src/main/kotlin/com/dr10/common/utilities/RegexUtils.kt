package com.dr10.common.utilities

import java.util.regex.Pattern

object RegexUtils {

    /**
     * Checks if the given pattern is valid
     *
     * @param pattern The pattern to check
     */
    fun isValidPattern(pattern: String): Pair<Boolean, String> = try {
        Pattern.compile(pattern)
        Pair(true, "")
    } catch (e: Exception) { Pair(false, e.message.toString()) }
}