package com.dr10.common.utilities

object OsUtils {

    // Gets the current OS and determines whether it is [Windows] or [Linux]
    private val osName: String = System.getProperty("os.name").lowercase()
    val isWindows: Boolean = osName.startsWith("windows")
    val isLinux: Boolean = osName.startsWith("linux")

}