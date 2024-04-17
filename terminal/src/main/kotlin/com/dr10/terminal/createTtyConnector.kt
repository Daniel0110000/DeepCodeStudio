package com.dr10.terminal

import com.jediterm.pty.PtyProcessTtyConnector
import com.jediterm.terminal.TtyConnector
import com.jediterm.terminal.ui.UIUtil
import com.pty4j.PtyProcessBuilder
import java.nio.charset.StandardCharsets

/**
 * Creates a TtyConnector for a terminal emulator, specifically configured for Zsh or PowerShell depending on the platform
 *
 * @param currentPath The current path where the terminal will be initiated
 * @return A TtyConnector instance for communication with the terminal emulator
 * @throws IllegalStateException if an exception occurs during the process creation
 */
fun createTtyConnector(currentPath: String): TtyConnector {
    return try {
        // Initialize environment variable and command for starting [zsh] or [PowerShell]
        val env: MutableMap<String, String> = HashMap(System.getenv())
        val command: Array<String>

        if(UIUtil.isWindows){
            // If the platform is Windows, it initializes with PowerShell
            command = arrayOf("powershell.exe")
        } else {
            // If the platform is not Windows, it initializes with Zsh
            command = arrayOf("/bin/zsh", "--login")
            // Set the terminal type to xterm-256color
            env["TERM"] = "xterm-256color"
        }

        // Start the PtyProcess with the configured command and environment
        val process = PtyProcessBuilder().setCommand(command).setEnvironment(env).setDirectory(currentPath).start()

        // Create a TtyConnector using the PtyProcess and UTF-8 encoding
        PtyProcessTtyConnector(process, StandardCharsets.UTF_8)
    } catch (e: Exception) {
        throw IllegalStateException(e)
    }
}